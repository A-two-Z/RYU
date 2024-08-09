using System;
using System.Collections;
using System.Collections.Generic;
using System.Net;
using System.Text.RegularExpressions;
using TMPro;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using static System.Net.WebRequestMethods;

public class Data : MonoBehaviour
{
    public static Data Instance { get; private set; }

    public string ServerUrl;
    public string ROSUrl;

    public GameObject inputText;
    public GameObject warningText;

    private float timer;

    public void ExitProgram()
    {
        Application.Quit();

#if UNITY_EDITOR
        UnityEditor.EditorApplication.isPlaying = false;
#endif
    }
    public void GoLoginScene()
    {
        SceneManager.LoadScene("LoginScene");
    }
    public void GoNextScene()
    {
        // TextMeshProUGUI���� �ؽ�Ʈ�� ��������
        string str = inputText.GetComponent<TextMeshProUGUI>().text.Trim();

        str = str.Replace("\u200B", "").Trim();

        // ���� ǥ���� ����
        string pattern = @"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Regex regex = new Regex(pattern);

        // ���� ��ġ
        if (regex.IsMatch(str))
        {
            ROSUrl = "ws://" + str + ":9090";
            SceneManager.LoadScene("MainScene");
        }
        else
        {
            warningText.GetComponent<TextMeshProUGUI>().text = "Is Not Valid IP Address";
            warningText.gameObject.SetActive(true);
            timer = 0.0f;
        }
    }

    private void Awake()
    {
        // �ı����� �ʴ� ������Ʈ�� �̱������� ����
        if (Instance == null)
        {
            Instance = this;
            DontDestroyOnLoad(gameObject);
        }
        else
        {
            Destroy(gameObject);
        }
    }
    private void Start()
    {
        // ��� �޽��� �ʱ�ȭ
        warningText.GetComponent<TextMeshProUGUI>().text = "";
        timer = 0.0f;

        ServerUrl = "https://i11a201.p.ssafy.io/productSector/sectorInfo";
    }

    // Update is called once per frame
    void Update()
    {
        if (SceneManager.GetActiveScene().name == "LoginScene")
        {
            if (inputText == null)
            {
               inputText = GameObject.Find("Canvas").transform.Find("Panel").Find("BackGround").Find("IPAddressInputField").Find("Text Area").Find("Text").gameObject;
            }
            if (warningText == null)
            {
                warningText = GameObject.Find("Canvas").transform.Find("Panel").Find("BackGround").Find("Warning").gameObject;
            }
            
            timer += Time.deltaTime;
            if (timer > 5.0f && warningText != null) warningText.gameObject.SetActive(false);
        }

        // ���콺 ���� ��ư Ŭ���� ���Դٸ�
        if (Input.GetMouseButtonDown(0))
        {
            // PointerEventData ����
            PointerEventData pointerData = new PointerEventData(EventSystem.current)
            {
                position = Input.mousePosition
            };

            // RaycastAll�� ����Ͽ� ��� UI ������Ʈ ����
            List<RaycastResult> raycastResults = new List<RaycastResult>();
            EventSystem.current.RaycastAll(pointerData, raycastResults);

            if (raycastResults.Count > 0)
            {
                // UI ������Ʈ Ŭ�� ó��
                // UI�� Ȱ��ȭ�Ǿ� �ִ� ���, UI�� ������ �ϴ� �Լ� ȣ��
                // Ŭ���� UI ������Ʈ
                GameObject clickedUIObject = raycastResults[0].gameObject;

                if (raycastResults[0].gameObject.layer == LayerMask.NameToLayer("UI"))
                {
                    // UI ��Ʈ�ѷ��� ���� �гο��� ã��
                    Transform parentTransform = clickedUIObject.transform;
                    UIController uiController = null;

                    while (parentTransform != null)
                    {
                        uiController = parentTransform.GetComponent<UIController>();
                        if (uiController != null)
                        {
                            break;
                        }
                        parentTransform = parentTransform.parent;
                    }

                    // UIController�� �ִ� ��� UI�� �Ѱų� ���� �Լ� ȣ��
                    if (uiController != null)
                    {
                        uiController.UIOnOff(uiController.gameObject);
                    }
                }
            }
            else
            {
                // UI ������Ʈ Ŭ���� ���� ��� 3D ������Ʈ ó��
                Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
                RaycastHit[] hits = Physics.RaycastAll(ray, Mathf.Infinity);

                if (hits.Length > 0)
                {
                    // ���� ����� ��ü�� ����
                    RaycastHit closestHit = hits[0];

                    foreach (RaycastHit hit in hits)
                    {
                        if (hit.distance < closestHit.distance)
                        {
                            closestHit = hit;
                        }
                    }

                    int layer = closestHit.collider.gameObject.layer;

                    if (layer == LayerMask.NameToLayer("Robot"))
                    {
                        closestHit.transform.GetComponent<RosSubscriber>().UIOnOff();
                    }
                    else if (layer == LayerMask.NameToLayer("Sector"))
                    {
                        closestHit.transform.GetComponent<Sector>().UIOnOff();
                    }
                }
            }
        }
    }
}
