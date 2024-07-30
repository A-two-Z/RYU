using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using RosSharp.RosBridgeClient;
using RosSharp.RosBridgeClient.MessageTypes.Std;
using Unity.VisualScripting;
using System;

public class TestSubscriber : UnitySubscriber<RosSharp.RosBridgeClient.MessageTypes.Std.String>
{
    public float moveSpeed = 1.0f; // �̵� �ӵ�
    public float rotationSpeed = 1.0f; // ȸ�� �ӵ�
    public float moveThreshold = 0.1f; // ��ġ ���� ���� �Ÿ�
    public float rotationThreshold = 0.1f; // ȸ�� ���� ���� ����

    private Vector3 targetPosition;
    private Quaternion targetRotation;
    private bool isMoving = false;
    private bool isRotating = false;

    public float minX = 8.0f; // x�� �ּ� ����
    public float maxX = 40.0f; // x�� �ִ� ����
    public float minZ = 5.0f; // z�� �ּ� ����
    public float maxZ = 62.0f; // z�� �ִ� ����

    protected override void ReceiveMessage(RosSharp.RosBridgeClient.MessageTypes.Std.String message)
    {
        Debug.Log("Message Received: " + message.data); // �ֿܼ� �޽��� ���

        try
        {
            // �޽��� �����͸� �Ľ�
            string[] parts = message.data.Split(',');
            if (parts.Length != 7) // �� 7���� ���� �־�� �մϴ�.
            {
                Debug.LogError("Invalid message format: Expected 7 parts separated by commas.");
                return;
            }

            // ��ġ ������ �Ľ�
            if (!float.TryParse(parts[0], out float x) ||
                !float.TryParse(parts[1], out float y) ||
                !float.TryParse(parts[2], out float z))
            {
                Debug.LogError("Invalid position data: Could not parse float values.");
                return;
            }
            targetPosition = new Vector3(x, y, z);

            // ȸ�� ������ �Ľ�
            if (!float.TryParse(parts[3], out float xRot) ||
                !float.TryParse(parts[4], out float yRot) ||
                !float.TryParse(parts[5], out float zRot) ||
                !float.TryParse(parts[6], out float wRot))
            {
                Debug.LogError("Invalid rotation data: Could not parse float values.");
                return;
            }
            targetRotation = new Quaternion(xRot, yRot, zRot, wRot);

            // �̵��� ȸ�� ����
            isMoving = true;
            isRotating = true;

            Debug.Log($"Target Position: {targetPosition}");
            Debug.Log($"Target Rotation: {targetRotation}");
        }
        catch (Exception ex)
        {
            Debug.LogError($"Exception occurred while parsing message: {ex.Message}");
        }
    }

    // Start is called before the first frame update
    void Start()
    {
        base.Start();
    }

    // Update is called once per frame
    void Update()
    {
        if (isMoving)
        {
            // ���� ��ġ���� ��ǥ ��ġ�� �̵�
            transform.position = Vector3.MoveTowards(transform.position, targetPosition, moveSpeed * UnityEngine.Time.deltaTime);

            // ��ġ ���� �� �̵� ���߱�
            if (Vector3.Distance(transform.position, targetPosition) < moveThreshold)
            {
                transform.position = targetPosition;
                isMoving = false;
            }
        }

        if (isRotating)
        {
            // ���� ȸ������ ��ǥ ȸ������ ȸ��
            transform.rotation = Quaternion.RotateTowards(transform.rotation, targetRotation, rotationSpeed * UnityEngine.Time.deltaTime);

            // ȸ�� ���� �� ȸ�� ���߱�
            if (Quaternion.Angle(transform.rotation, targetRotation) < rotationThreshold)
            {
                transform.rotation = targetRotation;
                isRotating = false;
            }
        }

        // ��ġ ���� ����
        ClampPosition();
    }

    private void ClampPosition()
    {
        // ���� ��ġ
        Vector3 pos = transform.position;

        // x�� ���� ����
        pos.x = Mathf.Clamp(pos.x, minX, maxX);

        // z�� ���� ����
        pos.z = Mathf.Clamp(pos.z, minZ, maxZ);

        // ���ѵ� ��ġ�� ����
        transform.position = pos;
    }
}
