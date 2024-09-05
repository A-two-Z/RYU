import rclpy
from rclpy.node import Node
from geometry_msgs.msg import Twist
from .submodules.myutil import PCA9685
import time
import paho.mqtt.client as mqtt

class PrintNode(Node):
    def __init__(self):
        super().__init__('chase_traffic_yolo')
        self._right = PCA9685(channel=8, address=0x60, busnum=1, center=380)
        self._left = PCA9685(channel=4, address=0x60, busnum=1, center=380)
        self._center = PCA9685(channel=7, address=0x60, busnum=1, center=380)

        # Publisher for cmd_vel
        self.cmd_vel_publisher = self.create_publisher(Twist, 'cmd_vel', 10)
        # MQTT client setup
        self.mqtt_client = mqtt.Client()
        self.mqtt_client.on_message = self.on_message_from_server
        self.mqtt_client.connect("54.180.232.169")
        self.mqtt_client.subscribe("docking/distance")
        self.mqtt_client.loop_start()
        self.flag = 1
        twist_msg = Twist()
        twist_msg.linear.x = 0.0  # Reverse speed
        twist_msg.angular.z = 0.0  # No rotation
        
        for i in range(1,100):
            self.cmd_vel_publisher.publish(twist_msg)
        

    def on_message_from_server(self, client, userdata, message):
        print(0)
        if message.topic == "docking/distance":
            msg_from_server = str(message.payload.decode("utf-8"))
            if float(msg_from_server) >= 22 and self.flag == 1:
                print(f"Received distance message: {msg_from_server}")
                twist_msg = Twist()
                twist_msg.linear.x = 0.5  # Reverse speed
                twist_msg.angular.z = 0.0  # No rotation
                self.cmd_vel_publisher.publish(twist_msg)
            elif self.flag == 1:
            
                self.flag = 2
                print(msg_from_server)
                self.mqtt_client.unsubscribe("docking/distance")
                msg_from_server = "222"
                print(1)
                # ∏ÿ√„
                twist_msg = Twist()
                twist_msg.linear.x = 0.0  # Reverse speed
                twist_msg.angular.z = 0.0  # No rotation
                self.cmd_vel_publisher.publish(twist_msg)
                time.sleep(1)
                
                #≥ª∏Æ
                for i in range(120, 381):
                    self._right.run(i)
                time.sleep(1)
        
                for i in range(650, 379, -1):
                    self._left.run(i)
                time.sleep(1)
        
                for i in range(410, 329, -1):
                    self._center.run(i)
                    time.sleep(0.05)
                time.sleep(2)
        
                for i in range(330, 411):
                    self._center.run(i)
                time.sleep(1)
        
                for i in range(380, 119, -1):
                    self._right.run(i)
                time.sleep(1)
        
                for i in range(380, 651):
                    self._left.run(i)
                time.sleep(1)
        
                
                # Send cmd_vel message
                twist_msg = Twist()
                twist_msg.linear.x = -0.5  # Reverse speed
                twist_msg.angular.z = 0.0  # No rotation
                
                for i in range(1,150):
                    self.cmd_vel_publisher.publish(twist_msg)
                    
                
                print(2)
                # Sleep for a short duration to ensure the message is sent
                time.sleep(3)
                twist_msg = Twist()
                twist_msg.linear.x = 0.0  # Reverse speed
                twist_msg.angular.z = 0.0  # No rotation
                self.cmd_vel_publisher.publish(twist_msg)
        
                
                print(3)
                time.sleep(2)
            elif self.flag == 2:
                self.mqtt_client.publish("robot/R1/status", "complete")
                rclpy.shutdown()
                      

def main(args=None):
    rclpy.init(args=args)
    node = PrintNode()

    try:
        rclpy.spin(node)  # Keep the node running to listen for messages
    except KeyboardInterrupt:
        pass
    finally:
        node.destroy_node()
        rclpy.shutdown()

if __name__ == '__main__':
    main()
