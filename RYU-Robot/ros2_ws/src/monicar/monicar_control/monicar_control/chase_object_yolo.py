import rclpy
from rclpy.node import Node
from rclpy.parameter import Parameter
from rclpy.logging import get_logger
from geometry_msgs.msg import Twist, PoseStamped, Pose
from nav2_msgs.action import NavigateToPose
from rclpy.action import ActionClient
from rclpy.duration import Duration
from .submodules.myutil import clamp, PCA9685, PWMThrottleRacer, PWMThrottle2Wheel, PWMThrottleHat, PWMSteering
import time
import re
import paho.mqtt.client as mqtt
from nav2_simple_commander.robot_navigator import BasicNavigator, TaskResult
import subprocess
import math
IIC_BUS = 1

class VehicleNode(Node):
    def __init__(self):
        super().__init__('GOAL_node')
        self.declare_parameters(
            namespace='',
            parameters=[
                ('has_steer', 1),
                ('isDCSteer', 0),
                ('steer_center', 380),
                ('steer_limit', 100),
                ('speed_center', 0),
                ('speed_limit', 4096),
                ('i2cSteer', 64),
                ('i2cThrottle', 96),
            ])
        self.get_logger().info("Setting Up the Node...")

        self.hasSteer = self.get_parameter_or('has_steer').get_parameter_value().integer_value
        self.isDCSteer = self.get_parameter_or('isDCSteer').get_parameter_value().integer_value
        self.STEER_DIR = self.get_parameter_or('steer_dir').get_parameter_value().integer_value

        self.STEER_CENTER = self.get_parameter_or('steer_center').get_parameter_value().integer_value
        self.STEER_LIMIT = self.get_parameter_or('steer_limit').get_parameter_value().integer_value
        self.SPEED_CENTER = self.get_parameter_or('speed_center').get_parameter_value().integer_value
        self.SPEED_LIMIT = self.get_parameter_or('speed_limit').get_parameter_value().integer_value

        self.i2cSteer = self.get_parameter_or('i2cSteer').get_parameter_value().integer_value
        self.i2cThrottle = self.get_parameter_or('i2cThrottle').get_parameter_value().integer_value

        self._right = PCA9685(channel=8, address=0x60, busnum=1, center=380)
        self._left = PCA9685(channel=4, address=0x60, busnum=1, center=380)
        self._center = PCA9685(channel=7, address=0x60, busnum=1, center=380)
        
        # 처음 중간 서보 힘주기
        self._center.run(420)
        
        self._client = ActionClient(self, NavigateToPose, 'navigate_to_pose')

        # 각 섹터별 좌표 (x,y,w,z)
        self.goals = {
            "A001": [(1.7, 0.15, 0.7, 0.7)],
            "B001": [(0.27, 0.75, 0.7, 0.7)],
            "B002": [(0.93, 0.27, 0.0, 1.0)],
            "B003": [(1.50, 0.90,0.7, 0.7)],
            "C001": [(0.08, 1.35, 0.0, 1.0)],
            "C002": [(0.9, 1.4, 0.0, 1.0)],
            "con": [(1.8, 1.3, 0.7, 0.7)],
            "ori":[(0.0,-0.5,0.0,1.0)]
        }

        # 현재 목표
        self.current_goal_index = 0
        self.current_goal_name = None
        # 계산 완료된 목표 순서대로 저장
        self.goal_points = []

        # mqtt 초기
        self.mqtt_client = mqtt.Client()
        self.mqtt_client.on_message = self.on_message_from_server
        self.mqtt_client.connect("54.180.232.169")
        self.mqtt_client.subscribe("robot/R1/info")
        self.mqtt_client.subscribe("robot/R1/status")
        self.mqtt_client.loop_start()

        self.sector_names = []
        self.product_names = []
        self.order_quantities = []
        self.current_index = 0
        self.waiting_for_complete = False

        # cmd_vel 퍼블리셔 초기화
        self.cmd_vel_publisher = self.create_publisher(Twist, 'cmd_vel', 10)
        
        
        #self.navigator = BasicNavigator()
        #self.declare_parameter('goal_checker.yaw_goal_tolerance', 0.5)
        #self.update_goal_checker_param(0.9)
        self.last_dance = False
        self.accept = False
        
        self.nownumber = 0
                

    def create_header(self):
        header = PoseStamped().header
        header.frame_id = 'map'
        return header

    # 목표 설정 후 리턴
    def create_pose(self, x, y, z, w):
        pose = Pose()
        pose.position.x = x
        pose.position.y = y
        pose.position.z = 0.0
        pose.orientation.x = 0.0
        pose.orientation.y = 0.0
        pose.orientation.z = z
        pose.orientation.w = w
        return pose
        
    # 목적지 보내기
    def send_goal(self, goal_pose):
        self._client.wait_for_server()

        goal_msg = NavigateToPose.Goal()
        goal_msg.pose = PoseStamped()
        goal_msg.pose.header.frame_id = 'map'
        goal_msg.pose.pose = goal_pose

        self._send_goal_future = self._client.send_goal_async(goal_msg)
        self._send_goal_future.add_done_callback(self.goal_response_callback)

    def goal_response_callback(self, future):
        goal_handle = future.result()
        if not goal_handle.accepted:
            self.get_logger().info('Goal rejected :(')
            return

        self.get_logger().info('Goal accepted :)')

        self._get_result_future = goal_handle.get_result_async()
        self._get_result_future.add_done_callback(self.get_result_callback)

    # 골 도착 했을때의 동작 지정
    def get_result_callback(self, future):
        result = future.result().result
        if future.result().status == 4:  # STATUS_SUCCEEDED
            self.get_logger().info('Goal reached, waiting for next command')
            print(self.last_dance)
            self.waiting_for_complete = True
            # 아직 섹터 남아있을때 
            if self.current_index < len(self.sector_names):  
                self.accept = False      
                self.repeat_publish_status()
            self.current_goal_index += 1
            if self.last_dance == True:
                print("ssss")
                command = ["ros2", "run", "monicar_control", "blob_chase"]
                result = subprocess.run(command, capture_output=True, text=True)
                self.last_dance = False
                self.mqtt_client.publish("robot/R1/clear", self.nownumber) 
                return
            
            # 남은 목적지가 없을때
            if self.current_goal_index >= len(self.goal_points) and self.current_index >= len(self.sector_names):           
                self.get_logger().info('Starting external node...')
                command = ["ros2", "run", "monicar_control", "chase_the_ball"]
                result = subprocess.run(command, capture_output=True, text=True)
                
                self.last_dance = True

            else:
                self.get_logger().info('Waiting for complete message...')
        elif future.result().status == 6:  # STATUS_ABORTED
            self.get_logger().info('Goal failed, sending reverse command')
            self.send_reverse_command()
    
    def update_goal_checker_param(self, yaw_tolerance):
        self.set_parameters([
            rclpy.parameter.Parameter(
                'goal_checker.yaw_goal_tolerance',
                rclpy.Parameter.Type.DOUBLE,
                yaw_tolerance
            )
        ])
        self.get_logger().info(f'Updated yaw_goal_tolerance to {yaw_tolerance}')

    def send_reverse_command(self):
        twist_msg = Twist()
        for i in range(1,20):
            twist_msg.linear.x = -0.5  # 후진 속도 설정
            twist_msg.angular.z = 0.0
            self.cmd_vel_publisher.publish(twist_msg)
        
        twist_msg = Twist()
        twist_msg.linear.x = 0.0  # 후진 속도 설정
        twist_msg.angular.z = 0.0
        
        self.cmd_vel_publisher.publish(twist_msg)
        
        
        if self.last_dance == True:
            self.send_goal(self.create_pose(0.0, -0.1, 0.0, 1.0))
        else:
            x, y, z, w = self.goal_points[self.current_goal_index]
            self.send_goal(self.create_pose(x, y, z, w))

    def repeat_publish_status(self):
        print("repeatin")
        # 특정 시간마다 MQTT 메시지를 보냄
        while not self.accept:
            print("repeat")
            self.mqtt_client.publish(f"sector/{self.sector_names[self.current_index]}", f"R1,{self.product_names[self.current_index]}/{self.order_quantities[self.current_index]}")
            time.sleep(1)  # 1초 간격으로 메시지 발행
            
        self.current_index += 1
        
        

    # MQTT 가 들어왔을 때 처리
    def on_message_from_server(self, client, userdata, message):
        msg_from_server = str(message.payload.decode("utf-8"))

        # 로봇 최초 시작
        if message.topic == "robot/R1/info":
            self.mqtt_client.publish("robot/R1/status", "start")
            arr = []
            self.goal_points = arr
            self.current_index=0
            self.current_goal_index=0
            # 메시지 파싱
            self.parsing(msg_from_server)

            # 현재 골 인덱스는 0으로 초기 설정
            self.current_goal_index = 0
            
            if self.sector_names:
                # 현재 골 이름 0으로 설정
                for i in range(0, len(self.sector_names)):
                    if i == 0:
                        self.current_goal_name = self.sector_names[i]
                        self.goal_points = self.goals.get(self.current_goal_name, []) 
                    else:
                        self.current_goal_name = self.sector_names[i]
                        self.goal_points += self.goals.get(self.current_goal_name, []) 
                    
                self.sort_by_distance()
                self.goal_points += self.goals.get("con", [])

                # 골 포인트에 따라 지정 후 마지막은 항상 con으로
                print(self.goal_points)
                self.send_next_goal()

        # 섹터에서 완료 메시지 옴
        elif message.topic == "robot/R1/status":
            if msg_from_server == "complete":
                if self.waiting_for_complete:
                    self.waiting_for_complete = False
                    self.send_next_goal()
            elif msg_from_server == "accept":
                self.accept = True

    def send_next_goal(self):
        # 골 포인트 남아 있으면 
        if self.current_goal_index < len(self.goal_points):
            x, y, z, w = self.goal_points[self.current_goal_index]
            self.send_goal(self.create_pose(x, y, z, w))
        else:
            self.send_goal(self.create_pose(0.0,-0.1 ,0.0,1.0))
            self.mqtt_client.publish("robot/R1/status", "clear") 
            
            
            
    def calculate_distance(self,point1, point2):
        return math.sqrt((point1[0] - point2[0]) ** 2 + (point1[1] - point2[1]) ** 2)

    def sort_by_distance(self):
        current_position = (0, 0)  # 로봇의 초기 출발 위치
        sorted_sector_names = []
        sorted_product_names = []
        sorted_order_quantities = []
        sorted_goal_points = []
    
        remaining_indices = list(range(len(self.sector_names)))
    
        while remaining_indices:
            sector_distances = []
    
            # 남아있는 섹터에 대해 거리를 계산
            for i in remaining_indices:
                sector_name = self.sector_names[i]
                if sector_name in self.goals:
                    goal_position = self.goals[sector_name][0][:2]  # x, y 좌표만 사용
                    distance = self.calculate_distance(current_position, goal_position)
                    sector_distances.append((distance, i))
    
            # 가장 가까운 섹터를 선택
            sector_distances.sort()
            closest_index = sector_distances[0][1]
    
            # 선택된 섹터의 정보를 정렬된 리스트에 추가
            sorted_sector_names.append(self.sector_names[closest_index])
            sorted_product_names.append(self.product_names[closest_index])
            sorted_order_quantities.append(self.order_quantities[closest_index])
            sorted_goal_points.append(self.goal_points[closest_index])
    
            # 현재 위치를 업데이트
            current_position = self.goal_points[closest_index][:2]
    
            # 이미 처리한 인덱스를 리스트에서 제거
            remaining_indices.remove(closest_index)

        # 최종적으로 정렬된 리스트를 할당
        self.sector_names = sorted_sector_names
        self.product_names = sorted_product_names
        self.order_quantities = sorted_order_quantities
        self.goal_points = sorted_goal_points
    
        self.get_logger().info(f"Sorted Sector Names: {self.sector_names}")
        self.get_logger().info(f"Sorted Product Names: {self.product_names}")
        self.get_logger().info(f"Sorted Order Quantities: {self.order_quantities}")
        self.get_logger().info(f"Sorted Goal Points: {self.goal_points}")

    # 파싱 함수
    def parsing(self, data_str):
      # orderNumber 추출
        order_number_pattern = re.compile(r'"orderNumber":\s*"(\d+)"')
        sector_pattern = re.compile(r'"sectorName":\s*"(.*?)"')
        product_pattern = re.compile(r'"productName":\s*"(.*?)"')
        quantity_pattern = re.compile(r'"orderQuantity":\s*(\d+)')
    
        # orderNumber 추출
        order_number_match = order_number_pattern.search(data_str)
        if order_number_match:
            self.nownumber = order_number_match.group(1)
        else:
            self.nownumber = None  # orderNumber가 없는 경우 처리
        
        # 섹터와 제품 이름 및 수량 추출
        self.sector_names = []
        self.product_names = []
        self.order_quantities = []
    
        sector_matches = sector_pattern.findall(data_str)
        product_matches = product_pattern.findall(data_str)
        quantity_matches = list(map(int, quantity_pattern.findall(data_str)))
    
        # 각 섹터에 해당하는 제품과 수량을 적절하게 매칭
        product_index = 0
        for sector in sector_matches:
            # 각 섹터에 있는 제품을 하나씩 추가
            num_products_in_sector = len(re.findall(r'"sectorName":\s*"' + re.escape(sector) + r'".*?"rabbitMQOrderDetails":\s*\[(.*?)\]', data_str, re.DOTALL)[0].split('},{'))
            for _ in range(num_products_in_sector):
                self.sector_names.append(sector)
                self.product_names.append(product_matches[product_index])
                self.order_quantities.append(quantity_matches[product_index])
                product_index += 1
    
        self.get_logger().info(f"Order Number: {self.nownumber}")
        self.get_logger().info(f"Sector Names: {self.sector_names}")
        self.get_logger().info(f"Product Names: {self.product_names}")
        self.get_logger().info(f"Order Quantities: {self.order_quantities}")
        


def main(args=None):
    rclpy.init(args=args)
    myCar = VehicleNode()
    rclpy.spin(myCar)
    myCar.destroy_node()
    rclpy.shutdown()

if __name__ == "__main__":
    main()
