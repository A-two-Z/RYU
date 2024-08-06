using UnityEngine;
using RosSharp.RosBridgeClient;
using RosSharp.RosBridgeClient.MessageTypes.Tf2;
using RosSharp.RosBridgeClient.MessageTypes.Geometry;
using System.Linq;

public class RosTestSubscriber : MonoBehaviour
{
    public GameObject UIPanel;

    private RosSocket rosSocket;
    public string socketURL;
    public string topicName; // ������ ���� �̸�

    // �̵� �� ȸ���� ���� ����
    public float moveSpeed;// �̵� �ӵ�
    public float rotationSpeed; // ȸ�� �ӵ�

    public UnityEngine.Vector3 targetPosition;
    //public UnityEngine.Vector3 targetRotation;
    public UnityEngine.Quaternion targetRotation;
    private bool isMoving = false;
    private bool isRotating = false;

    private Rigidbody rb;

    public int RobotID;
    public float RobotTemperature;
    public float WorkTime;

    private void Start()
    {
        RobotID = Random.Range(0, 10000000);
        RobotTemperature = Random.Range(20.0f, 50.0f);
        WorkTime = 0;

        //rb = GetComponent<Rigidbody>();
        //rb.isKinematic = false; // ������ �̵��� ���
        //rb.freezeRotation = true; // ȸ�� ���� (�ɼ�)


        // ROS WebSocket ������ URL
        socketURL = "ws://192.168.172.149:9090"; //"ws://192.168.56.105:9090"; // ������ URL�� ���� �ʿ�
        rosSocket = new RosSocket(new RosSharp.RosBridgeClient.Protocols.WebSocketNetProtocol(socketURL));

        // ���� ����
        if (topicName != null) rosSocket.Subscribe<TFMessage>(topicName, ReceiveMessage);


        // Collider�� ������ �߰�
        if (GetComponent<Collider>() == null)
        {
            gameObject.AddComponent<BoxCollider>(); // BoxCollider �߰�
        }
    }

    private void ReceiveMessage(TFMessage message)
    {
        Debug.Log(message);

        if (message.transforms.Count() > 0)
        {
            // ù ��° TransformStamped �޽��� ����
            TransformStamped transform = message.transforms[0];

            Debug.Log("first msg : " + message.transforms[0]);


            //x ���� -> z ����
            //y ���� -> x ����
            // ��ġ ������ ���� (�Ҽ��� 4�ڸ� �ݿø�)

            // 0 -> ���� �� / 26 / 35
            // 0 -> ���� �� / 
            //UnityEngine.Vector3 translation = new UnityEngine.Vector3(
            //    //38
            //    35.0f + Mathf.Round(-(float)transform.transform.translation.y * 152000) / 10000.0f,
            //    0.0f, // Mathf.Round((float)transform.transform.translation.z * 10000) / 10000.0f, // Z�� Y�� ��ȯ
            //    5.0f + Mathf.Round((float)transform.transform.translation.x * 142000) / 10000.0f  // Y�� Z�� ��ȯ
            //);
            UnityEngine.Vector3 translation = new UnityEngine.Vector3(
                        38.0f + Mathf.Round(-(float)transform.transform.translation.y * 1550) / 100.0f,
                        0.0f, // Y���� ������ ����
                        5.0f + Mathf.Round((float)transform.transform.translation.x * 1450) / 100.0f
                    );

            Debug.Log("����");

            // ���ο� ��ǥ ��ġ ���
            targetPosition = translation;

            // translation �� ����� ���
            Debug.Log($"Translation - x: {transform.transform.translation.x}, y: {transform.transform.translation.y}, z: {transform.transform.translation.z}");
            Debug.Log($"Calculated Target Position - x: {targetPosition.x}, y: {targetPosition.y}, z: {targetPosition.z}");

            //����->z 0 w 1
            //����->z - 1 w 0
            //��->z 0.7 w 0.7
            //��->z - 0.7 w 0.7

            // ȸ�� ������ ���� (�Ҽ��� 4�ڸ� �ݿø�)
            float x = Mathf.Round((float)transform.transform.rotation.x * 10000) / 10000.0f;
            float y = Mathf.Round((float)transform.transform.rotation.y * 10000) / 10000.0f;
            float z = Mathf.Round((float)transform.transform.rotation.z * 10000) / 10000.0f;
            float w = Mathf.Round((float)transform.transform.rotation.w * 10000) / 10000.0f;


            // ���� ȸ�������κ��� ȸ�� Quaternion ����
            UnityEngine.Quaternion newRotation = new UnityEngine.Quaternion(0, -z, 0, w);

            // ���� ȸ���� ���ο� ȸ���� ��Ÿ�� ���� ȸ������ ���
            targetRotation = newRotation;
            // targetRotation = gameObject.transform.rotation * newRotation;

            // rotation �� ����� ���
            Debug.Log($"Rotation - x: {transform.transform.rotation.x}, y: {transform.transform.rotation.y}, z: {transform.transform.rotation.z}, w: {transform.transform.rotation.w}");
            Debug.Log($"Calculated Target Rotation - x: {targetRotation.eulerAngles.x}, y: {targetRotation.eulerAngles.y}, z: {targetRotation.eulerAngles.z}");

            // �̵� �� ȸ�� ����
            isMoving = true;
            isRotating = true;
        }
    }

    private float NormalizeAngle(float angle)
    {
        angle = angle % 360;
        if (angle < 0)
        {
            angle += 360;
        }
        return angle;
    }

    private void Update()
    {
        WorkTime += Time.deltaTime;

        if (Input.GetMouseButtonDown(0)) // ���콺 ���� ��ư Ŭ��
        {
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition); // ȭ�鿡�� Ŭ�� ��ġ�� ���� �߻�
            RaycastHit hit;

            if (Physics.Raycast(ray, out hit))
            {
                if (hit.transform == transform) // Ŭ���� ������Ʈ�� �� ��ũ��Ʈ�� ���� ������Ʈ���� Ȯ��
                {
                    UIPanel.GetComponent<UIController>().RobotInfoPanel(gameObject);
                    // UIOnOff �Լ� ����
                }
            }
        }

        if (isMoving)
        {
            // ���� ��ġ���� ��ǥ ��ġ�� �̵�
            float step = moveSpeed * Time.deltaTime; // �� ������ �̵� �Ÿ�
            transform.position = UnityEngine.Vector3.MoveTowards(transform.position, targetPosition, step);

            // ��ǥ ��ġ�� �����ϸ� �̵� ����
            if (UnityEngine.Vector3.Distance(transform.position, targetPosition) < 0.1f)
            {
                transform.position = targetPosition;
                isMoving = false;
            }
        }

        if (isRotating)
        {
            // ���� ȸ������ ��ǥ ȸ������ ȸ��
            // UnityEngine.Quaternion targetQuaternion = UnityEngine.Quaternion.Euler(targetRotation.eulerAngles);
            transform.rotation = UnityEngine.Quaternion.RotateTowards(transform.rotation, targetRotation, rotationSpeed * Time.deltaTime);

            // ��ǥ ȸ�� ���� �� ȸ�� ����
            if (UnityEngine.Quaternion.Angle(transform.rotation, targetRotation) < 1.0f)
            {
                transform.rotation = targetRotation;
                isRotating = false;
            }
        }
    }

    private void OnDestroy()
    {
        if (rosSocket != null)
        {
            rosSocket.Close();
        }
    }
}
