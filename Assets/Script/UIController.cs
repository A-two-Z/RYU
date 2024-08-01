using System.Collections.Generic;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
using UnityEngine.Networking;
using System.Collections;

public class UIController : MonoBehaviour, IPointerClickHandler
{
    public string apiUrlBase = "http://localhost:8080/productSector/sectorInfo?SectorName=";


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
        Debug.Log(obj.name);

        if (obj.name == "Menu") gameObject.SetActive(!gameObject.activeSelf);
        else if (obj == gameObject) gameObject.SetActive(false);
        else gameObject.SetActive(true);
    }
    private void Start()
    {
        gameObject.SetActive(false);
        // ����: SectorA�� �׽�Ʈ�ϴ� ���
        //Sector = new GameObject("SectorA");
        //SettingSector(Sector);
    }

    private void FixedUpdate()
    {
        if (gameObject.activeSelf)
        {
            if (this.name == "MullyuInfoPanel") UpdateItemQuantities(); // ������ ���� ������Ʈ
            else if (this.name == "RobotInfoPanel") transform.Find("RightBox").Find("RobotWorkTime").GetComponent<TextMeshProUGUI>().text = TranslateTime((int)target.GetComponent<RosTestSubscriber>().WorkTime);
        }
    }

    public void SettingSector(GameObject obj)
    {
        Sector = obj;
        // ���� ������ ����
        //FetchDataFromApi(Sector.name);
        
        if (Sector.name == "SectorA")
        {
            LoadDummyDataForSectorA();
        }
        else if (Sector.name == "SectorB")
        {
            LoadDummyDataForSectorB();
        }
        
    }

    private IEnumerator FetchDataFromApi(string sectorName)
    {
        string apiUrl = apiUrlBase + sectorName; // URL ����
        UnityWebRequest request = UnityWebRequest.Get(apiUrl);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.ConnectionError || request.result == UnityWebRequest.Result.ProtocolError)
        {
            Debug.LogError("Error fetching data: " + request.error);
        }
        else
        {
            string jsonResponse = request.downloadHandler.text;
            currentItems = JsonHelper.FromJson<ItemData>(jsonResponse);
            InitializeUI(currentItems);
        }
    }

    private void LoadDummyDataForSectorA()
    {
        currentItems = new ItemData[]
        {
            new ItemData { itemName = "chocolate", quantity = 10 },
            new ItemData { itemName = "galaxybook", quantity = 1 },
            new ItemData { itemName = "galaxyring", quantity = 1 },
            new ItemData { itemName = "galaxys24", quantity = 1 },
            new ItemData { itemName = "galaxywatch", quantity = 2 },
        };

        InitializeUI(currentItems);
    }

    private void LoadDummyDataForSectorB()
    {
        currentItems = new ItemData[]
        {
            new ItemData { itemName = "haribo", quantity = 100 },
            new ItemData { itemName = "dongwonchamchi", quantity = 20 },
            new ItemData { itemName = "chocolate", quantity = 20 },
        };

        InitializeUI(currentItems);
    }

    private void InitializeUI(ItemData[] items)
    {
        // ���� ������ �ʱ�ȭ
        slots.Clear();
        foreach (Transform child in content)
        {
            Destroy(child.gameObject);
        }

        // �� ������ �����ϰ� ����
        foreach (var item in items)
        {
            GameObject slot = Instantiate(slotPrefab, content);
            SlotController slotController = slot.GetComponent<SlotController>();
            slotController.Setup(item); // SlotController���� ������ ����
            slots.Add(slotController);
        }
    }

    private void UpdateItemQuantities()
    {
        // ���� �������� ������ �������� ����
        if (currentItems == null) return;

        // ������ ������ �������� ����
        foreach (var item in currentItems)
        {
            item.quantity += Random.Range(-1, 2);
            item.quantity = Mathf.Max(item.quantity, 0);

            // ������ ã�Ƽ� ������Ʈ
            SlotController slot = slots.Find(s => s.GetItemName() == item.itemName);
            if (slot != null)
            {
                slot.UpdateItem(item);
            }
        }
    }
}


[System.Serializable]
public class ItemData
{
    public string itemName;  // �����̸�
    public int quantity;     // ����
}

public static class JsonHelper
{
    public static T[] FromJson<T>(string json)
    {
        Wrapper<T> wrapper = JsonUtility.FromJson<Wrapper<T>>(json);
        return wrapper.Items;
    }

    [System.Serializable]
    private class Wrapper<T>
    {
        public T[] Items;
    }
}