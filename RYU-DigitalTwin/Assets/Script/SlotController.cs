using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class SlotController : MonoBehaviour
{
    // �޴����� �κ� ����Ʈ�� ������ �� �ʿ��� Ÿ�ٺ���
    public GameObject target;

    // ���Կ� ������ �����͵�
    // �̹���, �̸�, ����
    private Image image;
    private TextMeshProUGUI nameText;
    private int quantity;

    // Start is called before the first frame update
    void Start()
    {
        // �޴��� �ִ� ���Ե��� ���ۺ��� Ÿ���� ��� �����Ƿ� �ٷ� �ʱ�ȭ�ϰ� ���ķ� ������Ʈ ���� ����.
        if (target != null)
        {
            image = target.transform.Find("Image").GetComponent<Image>();
            name = target.name;

            if (image != null) transform.Find("Image").GetComponent<Image>().sprite = target.transform.Find("Image").GetComponent<Image>().sprite;
            if (name != null) transform.Find("Name").GetComponent<TextMeshProUGUI>().text = name;
        }
    }

    public void Setup(ItemData itemData)
    {
        // �������� ��������Ʈ�� ã�Ƽ� ����
        name = itemData.productName;
        quantity = itemData.productQuantity;

        // ������Ʈ �ʱ�ȭ
        image = transform.Find("Image").GetComponent<Image>();
        nameText = transform.Find("Name").GetComponent<TextMeshProUGUI>();
        this.name = itemData.productName;

        // ��������Ʈ ����
        Sprite sprite = Resources.Load<Sprite>($"Sprites/{name}");
        if (sprite != null)
        {
            image.sprite = sprite;
        }
        else
        {
            Debug.LogWarning($"Sprite not found for item: {name}");
        }

        // ���� �ؽ�Ʈ ����
        nameText.text = quantity.ToString();
    }

    public void UpdateItem(ItemData itemData)
    {
        // ���� �ؽ�Ʈ�� ������Ʈ
        nameText.text = itemData.productQuantity.ToString();
    }

    public string GetItemName()
    {
        return name;
    }
}