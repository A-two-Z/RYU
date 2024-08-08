using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraController : MonoBehaviour
{
    // �ʱ⿡ ī�޶� ����ٴ� Ÿ�� ����
    public GameObject initTarget;

    // ī�޶� ������Ʈ
    public Camera mainCamera;

    // �����̴� �ӵ��� ������ ������
    public float moveSpeed = 10f;
    public float lookSpeed = 2f;
    public float focusSpeed = 2f;

    // �ʱ� X ����ڰ� ������ Ÿ��
    public Transform target;

    // ī�޶� ������ �Ÿ�
    public Vector3 offset;

    // ����ٴ� Ÿ�� ���� �Լ�
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

        UpdateCameraPosition();
    }

    // ī�޶� ��ġ ������Ʈ
    private void UpdateCameraPosition()
    {
        if (target == null)
        {
            return; // Ÿ���� �������� �ʾҴٸ� ��ġ ������Ʈ���� ����
        }

        // ī�޶��� ��ġ�� ��󿡼� �����¸�ŭ ������ ���� ����
        Vector3 desiredPosition = target.position - target.forward * Mathf.Abs(offset.z) + Vector3.up * offset.y;
        // ī�޶� �ε巴�� �ش� ��ǥ�� �̵��� �� �ֵ��� ��.
        mainCamera.transform.position = Vector3.Lerp(mainCamera.transform.position, desiredPosition, Time.deltaTime * focusSpeed);

        // ī�޶� �׻� ����� �ٶ󺸵��� �����մϴ�.
        mainCamera.transform.LookAt(target.position);
    }
}
