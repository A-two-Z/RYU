using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

public class Sector : MonoBehaviour
{
    public GameObject UIPanel;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetMouseButtonDown(0)) // ���콺 ���� ��ư Ŭ��
        {
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition); // ȭ�鿡�� Ŭ�� ��ġ�� ���� �߻�
            RaycastHit hit;

            if (Physics.Raycast(ray, out hit))
            {
                if (hit.transform == transform) // Ŭ���� ������Ʈ�� �� ��ũ��Ʈ�� ���� ������Ʈ���� Ȯ��
                {
                    UIPanel.GetComponent<UIController>().UIOnOff(gameObject);
                    UIPanel.GetComponent<UIController>().SettingSector(gameObject);
                    // UIOnOff �Լ� ����
                }
            }
        }
    }
}
