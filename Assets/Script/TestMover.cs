using UnityEngine;

public class TestMover : MonoBehaviour
{
    public float moveSpeed = 1.0f; // ������ �ӵ�
    public float moveInterval = 5.0f; // ���� ���� ���� (�� ����)
    private Vector3 direction; // �̵� ����

    private float timeSinceLastChange = 0f; // ������ ���� ���� ���� ��� �ð�

    public float minX = 8.0f; // x�� �ּ� ����
    public float maxX = 40.0f; // x�� �ִ� ����
    public float minZ = 5.0f; // z�� �ּ� ����
    public float maxZ = 62.0f; // z�� �ִ� ����

    void Start()
    {
        // ��ǥ �ʱ�ȭ
        transform.position = new Vector3(23.0f, 0.0f, 23.0f);

        // ù ������ ����
        SetRandomDirection();
    }

    void Update()
    {
        // ���� �������� �̵�
        transform.position += direction * moveSpeed * Time.deltaTime;

        // ��ġ ���� Ȯ��
        ClampPosition();

        // ��� �ð� ������Ʈ
        timeSinceLastChange += Time.deltaTime;

        // ������ ������ �ð��� �����ߴ��� Ȯ��
        if (timeSinceLastChange >= moveInterval)
        {
            SetRandomDirection();
            timeSinceLastChange = 0f; // Ÿ�̸� ����
        }
    }

    void SetRandomDirection()
    {
        // 90��, 180��, 270�� �� �����ϰ� �����Ͽ� ȸ��
        float[] angles = { 90f, 180f, 270f };
        float randomAngle = angles[Random.Range(0, angles.Length)];

        // ���� ���⿡�� �����ϰ� ȸ��
        transform.Rotate(0, randomAngle, 0);

        // ���� ȸ�� ���⿡ ���� ���ο� �̵� ���� ����
        direction = transform.forward;
    }

    void ClampPosition()
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
