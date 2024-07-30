using UnityEngine;
using RosSharp.RosBridgeClient;
using RosSharp.RosBridgeClient.MessageTypes.Tf2;
using RosSharp.RosBridgeClient.MessageTypes.Geometry;
using System.Linq;

public class RosTestSubscriber : MonoBehaviour
{
    private RosSocket rosSocket;
    public string topicName = "/tf"; // ������ ���� �̸�

    // �̵� �� ȸ���� ���� ����
    public float moveSpeed = 2.0f; // �̵� �ӵ�
    public float rotationSpeed = 100.0f; // ȸ�� �ӵ�

    private UnityEngine.Vector3 targetPosition;
    private UnityEngine.Quaternion targetRotation;
    private bool isMoving = false;
    private bool isRotating = false;

    private void Start()
    {
        // ROS WebSocket ������ URL
        string rosBridgeUrl = "ws://192.168.56.105:9090"; // ������ URL�� ���� �ʿ�
        rosSocket = new RosSocket(new RosSharp.RosBridgeClient.Protocols.WebSocketNetProtocol(rosBridgeUrl));

        // ���� ����
        rosSocket.Subscribe<TFMessage>(topicName, ReceiveMessage);

        Debug.Log(rosBridgeUrl);
        Debug.Log(rosSocket);
    }

    private void ReceiveMessage(TFMessage message)
    {
        Debug.Log("Message Received"); // �޽��� ���� �α�
        Debug.Log(message);

        if (message.transforms.Count() > 0)
        {
            // ù ��° TransformStamped �޽��� ����
            TransformStamped transform = message.transforms[0];

            // ��ġ ������ ����
            targetPosition = new UnityEngine.Vector3(
                (float)transform.transform.translation.x,
                (float)transform.transform.translation.y,
                (float)transform.transform.translation.z);

            // ȸ�� ������ ����
            targetRotation = new UnityEngine.Quaternion(
                (float)transform.transform.rotation.x,
                (float)transform.transform.rotation.y,
                (float)transform.transform.rotation.z,
                (float)transform.transform.rotation.w);

            // �̵� �� ȸ�� ����
            isMoving = true;
            isRotating = true;
        }
        else
        {
            Debug.LogWarning("No transforms found in the received TFMessage.");
        }
    }

    private void Update()
    {
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
            UnityEngine.Quaternion targetQuaternion = UnityEngine.Quaternion.Euler(targetRotation.eulerAngles);
            transform.rotation = UnityEngine.Quaternion.RotateTowards(transform.rotation, targetQuaternion, rotationSpeed * Time.deltaTime);

            // ��ǥ ȸ�� ���� �� ȸ�� ����
            if (UnityEngine.Quaternion.Angle(transform.rotation, targetQuaternion) < 1.0f)
            {
                transform.rotation = targetQuaternion;
                isRotating = false;
            }
        }
    }

    private void OnDestroy()
    {
        if (rosSocket != null)
        {
            rosSocket.Unsubscribe(topicName);
            rosSocket.Close();
        }
    }
}
