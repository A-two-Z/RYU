using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ThirdPersonCamera : MonoBehaviour
{
    public Transform target; // ����ٴ� ��� (TestMover ������Ʈ)
    public Vector3 offset = new Vector3(0, 5, -10); // ī�޶�� ��� ������ �Ÿ�

    private void Start()
    {
        // �ʱ� ī�޶� ��ġ�� �����մϴ�.
        if (target != null)
        {
            // ī�޶� ����� �Ĺ����� �̵��մϴ�.
            UpdateCameraPosition();
        }
    }

    private void LateUpdate()
    {
        // ����� ȸ���� ���� ī�޶� ��ġ ������Ʈ
        if (target != null)
        {
            UpdateCameraPosition();
        }
    }

    private void UpdateCameraPosition()
    {
        // ī�޶��� ��ġ�� ����� �Ĺ����� �����մϴ�.
        transform.position = target.position - target.forward * Mathf.Abs(offset.z) + Vector3.up * offset.y;
        // ī�޶� �׻� ����� �ٶ󺸵��� �����մϴ�.
        transform.LookAt(target);
    }
}
