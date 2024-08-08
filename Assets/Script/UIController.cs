using System.Collections.Generic;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
using UnityEngine.Networking;
using System.Collections;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;


public class UIController : MonoBehaviour, IPointerClickHandler
{
    // http request�� ���� �ּ�
    public string apiUrlBase;
    // ���� ������ ǥ���� �� ���� ������ �������� �޾ƿͼ� �������ֱ� ���� ���� ������
    public GameObject slotPrefab;
    // ���� ������ �������� ǥ���� �� �����͸� ǥ���� �� �����̳�
    public Transform content;
    // ���� ������ �������� ǥ���� �� ǥ���� �����͵�
    private List<SlotController> slots = new List<SlotController>();
    // ���� ������ �迭
    private ItemData[] currentItems;
    // ���� ���õ� ����
    private GameObject Sector;

    // ���� ���õ� �κ� ���
    private GameObject target;

    public void OnPointerClick(PointerEventData eventData)
    {
        
    }
    private string TranslateTime(int timeData)
    {
        // �۾� �ð��� �������ؼ� �����ֱ� ���� ��ȯ �۾�
        int hours = timeData / 3600;
        int minutes = (timeData % 3600) / 60;
        int seconds = timeData % 60;

        // �� �ڸ� ���ڷ� ������
        string str = $"{hours:D2} : {minutes:D2} : {seconds:D2}";
        return str;
    }
    // �κ� ���� �г��� �����ϱ� ���� �Լ�
    public void RobotInfoPanel(GameObject obj)
    {
        target = obj;
        UIOnOff(true);
        if (this.name == "RobotInfoPanel")
        {
            // �κ����Լ� ������ �޾ƿͼ� ������Ʈ
            transform.Find("LeftBox").Find("Image").GetComponent<Image>().sprite = target.transform.Find("Image").GetComponent<Image>().sprite;
            transform.Find("LeftBox").Find("RobotName").GetComponent<TextMeshProUGUI>().text = target.name;

            transform.Find("RightBox").Find("RobotID").GetComponent<TextMeshProUGUI>().text = target.GetComponent<RosSubscriber>().RobotID.ToString();
            transform.Find("RightBox").Find("RobotTemperature").GetComponent<TextMeshProUGUI>().text = $"{target.GetComponent<RosSubscriber>().RobotTemperature:F1}��C";
            transform.Find("RightBox").Find("RobotWorkTime").GetComponent<TextMeshProUGUI>().text = TranslateTime((int)target.GetComponent<RosSubscriber>().WorkTime);
        }
    }

    // UI �Ѱ� ���� ���� �Լ�
    private void UIOnOff(bool val)
    {
        gameObject.SetActive(val);
    }

    // ������Ʈ�� �������� �� ����� ������ ������ �ʵ��� �ϱ� ���� ó�� �Լ�
    public void UIOnOff(GameObject obj)
    {
        if (obj.name == "Menu") gameObject.SetActive(!gameObject.activeSelf);
        else if (obj == gameObject) gameObject.SetActive(false);
        else gameObject.SetActive(true);
    }
    private void Start()
    {
        // ���۵��ڸ��� �޴� ��ư�� ������ ��� UI�� ����ڿ��� ���̸� �ȵȴ�
        gameObject.SetActive(false);
    }

    private void FixedUpdate()
    {
        // ������Ʈ�� Ȱ��ȭ�� ���¶��
        if (gameObject.activeSelf)
        {
            // ���� ������Ʈ
            if (this.name == "MullyuInfoPanel") FetchDataFromApi(Sector.name);
            else if (this.name == "RobotInfoPanel") transform.Find("RightBox").Find("RobotWorkTime").GetComponent<TextMeshProUGUI>().text = TranslateTime((int)target.GetComponent<RosSubscriber>().WorkTime);
        }
    }

    public void SettingSector(GameObject obj)
    {
        Sector = obj;
        // ������ ����
        // �����κ��� �����͸� �޾ƿͼ� �������ϴ� �۾��̹Ƿ� �ڷ�ƾ���� �񵿱� �۾����� ����
        StartCoroutine(FetchDataFromApi(Sector.name));
        
    }

    // �񵿱� �۾� ���� �����͸� �޾ƿ��� �Լ�
    private IEnumerator FetchDataFromApi(string sectorName)
    {
        string apiUrl = $"{apiUrlBase}?SectorName={sectorName}";
        UnityWebRequest request = UnityWebRequest.Get(apiUrl);
        yield return request.SendWebRequest();

        Debug.Log(apiUrl);

        if (request.result == UnityWebRequest.Result.ConnectionError || request.result == UnityWebRequest.Result.ProtocolError)
        {
            Debug.LogError("Error fetching data: " + request.error);
        }
        else
        {
            string jsonResponse = request.downloadHandler.text;

            // JSON �����͸� �Ľ��Ͽ� ItemData[]�� ��ȯ
            // �Ϲ������� �����Ǵ� JsonUtility�� Ŀ���� JSON���� ����� ��ȯ������ ���� ...
            try
            {
                JObject jsonObject = JObject.Parse(jsonResponse);

                // ���� �̸��� Ű�� ����Ͽ� ���� �����͸� ����
                JArray itemArray = (JArray)jsonObject[sectorName];

                // ItemData �迭�� ��ȯ
                List<ItemData> itemList = itemArray.ToObject<List<ItemData>>();

                // currentItems�� ������ �Ҵ�
                currentItems = itemList.ToArray();

                // UI �ʱ�ȭ
                InitializeUI(currentItems, sectorName);
            }
            catch (JsonException e)
            {
                Debug.LogError("Error parsing JSON: " + e.Message);
            }
        }
    }

    private void InitializeUI(ItemData[] items, string sectorName)
    {
        // ���� ���� ����
        slots.Clear();
        foreach (Transform child in content)
        {
            // ���� �̸� ������Ʈ
            if (child.gameObject.name == "SectorName")
            {
                child.gameObject.transform.GetComponent<TextMeshProUGUI>().text = "Sector" + sectorName;
                continue;
            }
            Destroy(child.gameObject);
        }

        // �� ���� ����
        foreach (var item in items)
        {
            GameObject slot = Instantiate(slotPrefab, content);
            SlotController slotController = slot.GetComponent<SlotController>();
            if (slotController != null)
            {
                slotController.Setup(item);
                slots.Add(slotController);
            }
            else
            {
                Debug.LogError("SlotController component missing on prefab.");
            }
        }
    }
}


[System.Serializable]
public class ItemData
{
    public string productName;  // �����̸�
    public int productQuantity;     // ����

    public ItemData(string name, int quantity)
    {
        productName = name;
        productQuantity = quantity;
    }
}