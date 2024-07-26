using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using RosSharp.RosBridgeClient;
using RosSharp.RosBridgeClient.MessageTypes.Std;
using Unity.VisualScripting;

public class TestSubscriber : UnitySubscriber<String>
{

    protected override void ReceiveMessage(String message)
    {
        Debug.Log("Message Received: " + message.data); // �ֿܼ� �޽��� ���
    }
    // Start is called before the first frame update
    void Start()
    {
        base.Start();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
