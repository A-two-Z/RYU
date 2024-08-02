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
    public string apiUrlBase = "http://mulryu.duckdns.org:8080/productSector/sectorInfo";


    public GameObject slotPrefab;
    public Transform content;
    private List<SlotController> slots = new List<SlotController>();
    private GameObject Sector;

    private GameObject target;
    // ���� ������ �迭
    private ItemData[] currentItems;

    public void OnPointerClick(PointerEventData eventData)
    {
        
    }
    private string TranslateTime(int timeData)
    {
        int hours = timeData / 3600;
        int minutes = (timeData % 3600) / 60;
        int seconds = timeData % 60;

        // �� �ڸ� ���ڷ� ������
        string str = $"{hours:D2} : {minutes:D2} : {seconds:D2}";
        return str;
    }
    public void RobotInfoPanel(GameObject obj)
    {
        target = obj;
        UIOnOff(true);
        if (this.name == "RobotInfoPanel")
        {
            transform.Find("LeftBox").Find("Image").GetComponent<Image>().sprite = target.transform.Find("Image").GetComponent<Image>().sprite;
            transform.Find("LeftBox").Find("RobotName").GetComponent<TextMeshProUGUI>().text = target.name;

            transform.Find("RightBox").Find("RobotID").GetComponent<TextMeshProUGUI>().text = target.GetComponent<RosTestSubscriber>().RobotID.ToString();
            transform.Find("RightBox").Find("RobotTemperature").GetComponent<TextMeshProUGUI>().text = $"{target.GetComponent<RosTestSubscriber>().RobotTemperature:F1}��C";
            transform.Find("RightBox").Find("RobotWorkTime").GetComponent<TextMeshProUGUI>().text = TranslateTime((int)target.GetComponent<RosTestSubscriber>().WorkTime);
        }
    }

    private void UIOnOff(bool val)
    {
        gameObject.SetActive(val);
    }
    public void UIOnOff(GameObject obj)
    {
        if (obj.name == "Menu") gameObject.SetActive(!gameObject.activeSelf);
        else if (obj == gameObject) gameObject.SetActive(false);
        else gameObject.SetActive(true);
    }
    private void Start()
    {
        gameObject.SetActive(false);
    }

    private void FixedUpdate()
    {
        if (gameObject.activeSelf)
        {
            if (this.name == "MullyuInfoPanel") FetchDataFromApi(Sector.name); // ������ ���� ������Ʈ
            else if (this.name == "RobotInfoPanel") transform.Find("RightBox").Find("RobotWorkTime").GetComponent<TextMeshProUGUI>().text = TranslateTime((int)target.GetComponent<RosTestSubscriber>().WorkTime);
        }
    }

    public void SettingSector(GameObject obj)
    {
        Sector = obj;
        // ���� ������ ����
        StartCoroutine(FetchDataFromApi(Sector.name));
        
    }

    private IEnumerator FetchDataFromApi(string sectorName)
    {
        string apiUrl = $"{apiUrlBase}?SectorName={sectorName}";
        UnityWebRequest request = UnityWebRequest.Get(apiUrl);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.ConnectionError || request.result == UnityWebRequest.Result.ProtocolError)
        {
            Debug.LogError("Error fetching data: " + request.error);
        }
        else
        {
            string jsonResponse = request.downloadHandler.text;

            // JSON �����͸� �Ľ��Ͽ� ItemData[]�� ��ȯ
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