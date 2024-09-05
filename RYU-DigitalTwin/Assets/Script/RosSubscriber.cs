using UnityEngine;
using RosSharp.RosBridgeClient;
using RosSharp.RosBridgeClient.MessageTypes.Tf2;
using RosSharp.RosBridgeClient.MessageTypes.Geometry;
using System.Linq;

public class RosSubscriber : MonoBehaviour
{
    // �κ��� ������ �� ���� UI ����
    public GameObject UIPanel;

    // ROS2 ���� ���� ����
    // ����, URL, ����
    private RosSocket rosSocket;
    private string socketURL;
    public string topicName;

    // �̵� �� ȸ���� ���� ����
    // �̵� �ӵ��� ȸ�� �ӵ�
    public float moveSpeed;
    public float rotationSpeed;

    // ��ǥ ��ǥ�� ��ǥ ȸ�� ����
    public UnityEngine.Vector3 targetPosition;
    public UnityEngine.Quaternion targetRotation;

    // ������ ���� ����
    private bool isMoving = false;
    private bool isRotating = false;

    // �κ��� ���� ������ �߰����� ������ ���� ����
    public int RobotID;
    public float RobotTemperature;
    public float WorkTime;

    private void Start()
    {
        socketURL = Data.Instance.ROSUrl;
        // ����� ���������� ����.
        // ���� ROS2 ������� �߰������� ������ �޾ƿͼ� �Ҵ��� ����.
        RobotID = Random.Range(0, 10000000);
        RobotTemperature = Random.Range(20.0f, 50.0f);
        WorkTime = 0;

        // ROS WebSocket ������ URL
        if (gameObject.name == "AtwoZ")
        {
            //socketURL = "ws://192.168.153.149:9090";
            rosSocket = new RosSocket(new RosSharp.RosBridgeClient.Protocols.WebSocketNetProtocol(socketURL));

            // ���� ����
            if (topicName != null) rosSocket.Subscribe<TFMessage>(topicName, ReceiveMessage);
        }
    }

    private void ReceiveMessage(TFMessage message)
    {
        if (message.transforms.Count() > 0)
        {
            // ù ��° TransformStamped �޽��� ����
            TransformStamped transform = message.transforms[0];

            //  ����      ����
            // x ���� -> z ����
            // y ���� -> x ����
            // ������ǥ�� ������ǥ�� ���������̰� �����Ƿ� ���� ���缭 ����
            // ��Ȯ������ ������ 1550, 1450���� ����
            // �Ҽ��� ���ڸ����� ������� ����
            // z 4 ~ 40
            // x 7 ~ 42
            UnityEngine.Vector3 translation = new UnityEngine.Vector3(
                        34.0f + (Mathf.Round(-(float)transform.transform.translation.y * 1550000) / 100000.0f),
                        0.0f, // Y���� ������ ����. ���ư� �� ����
                        4.0f + (Mathf.Round((float)transform.transform.translation.x * 1600000) / 100000.0f)
                    );

            if (translation.z < 4.0f) translation.z = 4.0f;
            else if (translation.z > 40.0f) translation.z = 40.0f;

            if (translation.x < 7.0f) translation.x = 7.0f;
            else if (translation.x > 42.0f) translation.x = 42.0f;

            // ���ο� ��ǥ ��ġ ����� �Ҵ�
            targetPosition = translation;

            // translation �� ����� ....
            Debug.Log($"Translation - x: {transform.transform.translation.x}, y: {transform.transform.translation.y}, z: {transform.transform.translation.z}");
            Debug.Log($"Calculated Target Position - x: {targetPosition.x}, y: {targetPosition.y}, z: {targetPosition.z}");

            // ���ǿ��� ������ ���� ������
            // ���� -> z   0   w 1
            // ���� -> z - 1   w 0
            // ��   -> z   0.7 w 0.7
            // ��   -> z - 0.7 w 0.7

            // ȸ�� ������ ���� (�Ҽ��� 4�ڸ� �ݿø�)
            float x = Mathf.Round((float)transform.transform.rotation.x * 100000) / 100000.0f;
            float y = Mathf.Round((float)transform.transform.rotation.y * 100000) / 100000.0f;
            float z = Mathf.Round((float)transform.transform.rotation.z * 100000) / 100000.0f;
            float w = Mathf.Round((float)transform.transform.rotation.w * 100000) / 100000.0f;


            // ���� ȸ�������κ��� ȸ�� Quaternion ����
            UnityEngine.Quaternion newRotation = new UnityEngine.Quaternion(0, -z, 0, w);

            // ȸ�� ��� �� �Ҵ�
            targetRotation = newRotation;

            // rotation �� ����� ....
            Debug.Log($"Rotation - x: {transform.transform.rotation.x}, y: {transform.transform.rotation.y}, z: {transform.transform.rotation.z}, w: {transform.transform.rotation.w}");
            Debug.Log($"Calculated Target Rotation - x: {targetRotation.eulerAngles.x}, y: {targetRotation.eulerAngles.y}, z: {targetRotation.eulerAngles.z}");

            // �̵� �� ȸ�� ������ ���� ���� ����
            isMoving = true;
            isRotating = true;
        }
    }

    public void UIOnOff()
    {
        UIPanel.GetComponent<UIController>().RobotInfoPanel(gameObject);
    }

    private void Awake()
    {

    }
    private void Update()
    {
        WorkTime += Time.deltaTime;

        if (UIPanel == null)
        {
            UIPanel = GameObject.Find("Canvas").transform.Find("RobotInfoPanel").gameObject;
        }

        // ������ �� �ִ� ���¶��
        if (isMoving)
        {
            // ���� ��ġ���� ��ǥ ��ġ�� �̵��� ��
            // �� ������ �̵� �Ÿ�
            float step = moveSpeed * Time.deltaTime;
            transform.position = UnityEngine.Vector3.MoveTowards(transform.position, targetPosition, step);

            // ��ǥ ��ġ�� �����ϸ� �̵� ����
            // ��ǥ��ġ�� ���� ��ġ�� ���̰� 0.1f���� �۴ٸ� ����
            if (UnityEngine.Vector3.Distance(transform.position, targetPosition) < 0.1f)
            {
                transform.position = targetPosition;
                isMoving = false;
            }
        }

        // ȸ���� �� �ִ� ���¶��
        if (isRotating)
        {
            // ���� ȸ������ ��ǥ ȸ������ ȸ��
            transform.rotation = UnityEngine.Quaternion.RotateTowards(transform.rotation, targetRotation, rotationSpeed * Time.deltaTime);

            // ��ǥ ȸ�� ���� �� ȸ�� ����
            // ���������� ��ǥ ȸ���� ���� ȸ�� ���̰� 1.0f���� �۴ٸ� ���� ( ȸ���� �� �� ū ������ �������൵ ū ���̰� ���� ���� )
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
