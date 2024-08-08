using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

public class Sector : MonoBehaviour
{
    // ���͸� Ŭ������ �� ���� UI ����
    public GameObject UIPanel;
    void Start()
    {
        
    }

    void Update()
    {
        // ���콺 ���� ��ư Ŭ���� ���Դٸ�
        if (Input.GetMouseButtonDown(0))
        {
            // ȭ�鿡�� Ŭ�� ��ġ�� ���� �߻�
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            RaycastHit hit;

            if (Physics.Raycast(ray, out hit))
            {
                // Ŭ���� ������Ʈ�� �� ��ũ��Ʈ�� ���� ������Ʈ���� Ȯ��
                if (hit.transform == transform)
                {
                    Debug.Log("��");
                    // UIOnOff �� ���� �Լ� ����
                    UIPanel.GetComponent<UIController>().UIOnOff(gameObject);
                    UIPanel.GetComponent<UIController>().SettingSector(gameObject);
                }
            }
        }
    }
}
