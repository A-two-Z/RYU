using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraController : MonoBehaviour
{
    public GameObject initTarget;

    public Camera mainCamera;
    public float moveSpeed = 10f;
    public float lookSpeed = 2f;
    public float focusSpeed = 2f;
    public Transform target;
    public Vector3 offset;

    public void SettingTarget(GameObject obj)
    {
        target = obj.GetComponent<Transform>();
        UpdateCameraPosition();
    }

    private void Start()
    {
        SettingTarget(initTarget);
    }

    void Update()
    {
        if (target == null)
        {
            return; // Ÿ���� �������� �ʾҴٸ� ������Ʈ���� ����
        }

        // ī�޶� ��ġ ������Ʈ
        UpdateCameraPosition();
    }

    private void UpdateCameraPosition()
    {
        if (target == null)
        {
            return; // Ÿ���� �������� �ʾҴٸ� ��ġ ������Ʈ���� ����
        }

        // ī�޶��� ��ġ�� ����� �Ĺ�� �������� �����մϴ�.
        Vector3 desiredPosition = target.position - target.forward * Mathf.Abs(offset.z) + Vector3.up * offset.y;
        mainCamera.transform.position = Vector3.Lerp(mainCamera.transform.position, desiredPosition, Time.deltaTime * focusSpeed);

        // ī�޶� �׻� ����� �ٶ󺸵��� �����մϴ�.
        mainCamera.transform.LookAt(target.position);
    }
}
