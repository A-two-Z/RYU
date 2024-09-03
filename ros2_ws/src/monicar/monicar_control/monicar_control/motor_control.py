#!/usr/bin/env python

"""
Node for control PCA9685 using teleop_twist_keyboard msg
referenced from donekycar
url : https://github.com/autorope/donkeycar/blob/dev/donkeycar/parts/actuator.py
"""

import time
import rclpy
from rclpy.node import Node
from rclpy.parameter import Parameter
from rclpy.logging import get_logger
from geometry_msgs.msg import Twist
from .submodules.myutil import clamp, PCA9685, PWMThrottleRacer, PWMThrottle2Wheel, PWMThrottleHat, PWMSteering

import rclpy
from rclpy.node import Node
from geometry_msgs.msg import Twist
import time
from .submodules.myutil import clamp, PCA9685, PWMThrottleRacer, PWMThrottle2Wheel, PWMThrottleHat, PWMSteering


IIC_BUS = 1

global speed_pulse
global steering_pulse

global flag

class VehicleNode(Node):
    
    
    def __init__(self):
        global flag
        flag = 1
        super().__init__('motor_control_node')
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

        speed_pulse = 0
        steering_pulse = 0
        
        print('hasSteer: %s, i2cSteer: %s, i2cThrottle: %s'%
            (self.hasSteer,
            self.i2cSteer,
            self.i2cThrottle)
        )

        #RCcar which has steering
        if self.hasSteer == 1:
            #Steer with DC motor driver
            if self.isDCSteer == 1:
                steer_controller = PCA9685(channel=0, address=self.i2cSteer, busnum=IIC_BUS, center=self.STEER_CENTER)
                self._steering = PWMSteering(controller=steer_controller, max_pulse=4095, zero_pulse=0, min_pulse=-4095)
            
            #Steer with servo motor
            else:
                self._steering = PCA9685(channel=0, address=self.i2cSteer, busnum=IIC_BUS, center=self.STEER_CENTER)
                
                self._steer2 = PCA9685(channel=8, address=self.i2cSteer, busnum=IIC_BUS, center=self.STEER_CENTER)
               
                
            self.get_logger().info("Steering Controller Awaked!!")

            throttle_controller = PCA9685(channel=0, address=self.i2cThrottle, busnum=IIC_BUS, center=self.SPEED_CENTER)
            if self.i2cThrottle == 0x60:
                #Throttle with Jetracer
                self._throttle = PWMThrottleRacer(controller=throttle_controller, max_pulse=4095, zero_pulse=0, min_pulse=-4095)
            else:
                #Throttle with Motorhat B
                self._throttle = PWMThrottleHat(controller=throttle_controller, max_pulse=4095, zero_pulse=0, min_pulse=-4095)
            self.get_logger().info("Throttle Controller Awaked!!")
            
            
 

        #2wheel RCcar
        else:
            throttle_controller = PCA9685(channel=0, address=self.i2cSteer, busnum=IIC_BUS,  center=self.SPEED_CENTER)
            self._throttle = PWMThrottle2Wheel(controller=throttle_controller, max_pulse=4095, zero_pulse=0, min_pulse=-4095)
            self.get_logger().info("2wheel Throttle Controller Awaked!!")

        self._teleop_sub = self.create_subscription(Twist, 'cmd_vel', self.cb_teleop, 10)
        self.get_logger().info("Keyboard Subscriber Awaked!! Waiting for keyboard/joystick...")

        self.timer = self.create_timer(1.5, self.timer_callback)
        self.forward = True
        self.right = True
        self.z = 0.0  # ÃÊ±âÈ­
        self.x =0.0
        




    def cb_teleop(self, msg):
        global flag
        self.z = msg.angular.z
        self.x = msg.linear.x
        print(flag)
        #self.get_logger().info("Received a /cmd_vel message!")
        self.get_logger().info("Components: [%0.2f, %0.2f]"%(msg.linear.x, msg.angular.z))
        
        # Do velocity processing here:
        # Use the kinematics of your robot to map linear and angular velocities into motor commands
        # linear.x: 0, 0+step...1.0, step=0.1
        speed_pulse = self.SPEED_CENTER + msg.linear.x*self.SPEED_LIMIT
        speed_pulse = clamp(speed_pulse, -self.SPEED_LIMIT+1, self.SPEED_LIMIT-1)

        # angular.z: 0, 0+step...1.0, step=0.1
        steering_pulse = self.STEER_CENTER - msg.angular.z*self.STEER_LIMIT
        steering_pulse = clamp(steering_pulse, self.STEER_CENTER - self.STEER_LIMIT, self.STEER_CENTER + self.STEER_LIMIT)
        
        if self.x != 0 : 
            self.forward = True
            print(
                    "speed_pulse : "
                    + str(speed_pulse)
                    + " / "
                    + "steering_pulse : "
                    + str(steering_pulse)
                )
                
            
            
            if speed_pulse > 0 and speed_pulse <=3000:
                speed_pulse = 3000
                print(1)
            elif speed_pulse <0 and speed_pulse >= -3000:
                speed_pulse = -3000
                print(2)    
            
            
            """       
            if steering_pulse >= 375:
                steering_pulse = 375
                if speed_pulse > 0:
                    speed_pulse =4050
                elif speed_pulse <0:
                    speed_pulse = -4050
            elif steering_pulse <= 325:
                steering_pulse = 325
                if speed_pulse >0:
                    speed_pulse =4050
                elif speed_pulse <0:
                    speed_pulse = -4050
            """ 
            """   
            if speed_pulse == 0 and steering_pulse != 350:
                if steering_pulse > 350:
                    steering_pulse = 250
                elif steering_pulse <350:
                    steering_pulse = 450
               
                speed_pulse = -3000
            """
            if self.hasSteer == 1:
                print(
                    "speed_pulse : "
                    + str(speed_pulse)
                    + " / "
                    + "steering_pulse : "
                    + str(steering_pulse)
                )
            else:
                 print(
                    "speed_pulse : "
                    + str(speed_pulse)
                    + " / "
                    + "steer % : "
                    + str(steering_pulse*100/self.STEER_LIMIT)
                )
    
    
            if self.hasSteer == 1:
                self._throttle.run(speed_pulse)
                self._steering.run(steering_pulse)
            else:
                self._throttle.run(speed_pulse,steering_pulse)
        
        if self.x==0 and self.z==0:
            self._throttle.run(0)
            self._steering.run(350)            

            
    def timer_callback(self):
        if self.z!=0 and self.x==0:

            self._throttle.run(0)
            if self.z < 0:
                print(flag) 
                print("turn1")
                if self.forward:
                    for i in range(350, 451):
                        self.steering_pulse = i
                        self._steering.run(self.steering_pulse)
                        time.sleep(0.001)
                else:
                    for i in range(450, 249, -1):
                        self.steering_pulse = i
                        self._steering.run(self.steering_pulse)
                        time.sleep(0.001)
            elif self.z > 0:
                print("turn2")
                if self.forward:
                    for i in range(350, 249, -1):
                        self.steering_pulse = i
                        self._steering.run(self.steering_pulse)
                        time.sleep(0.001)
                else:
                    for i in range(250, 451):
                        self.steering_pulse = i
                        self._steering.run(self.steering_pulse)
                        time.sleep(0.001)
                        

            if self.forward:
                self.speed_pulse = 3000
            else:
                self.speed_pulse = -3000
            
            print(self.speed_pulse)
            self._throttle.run(self.speed_pulse)
            self.forward = not self.forward
            

def main(args=None):
    rclpy.init(args=args)
    myCar = VehicleNode()
    rclpy.spin(myCar)
    myCar.destroy_node()
    rclpy.shutdown()

if __name__ == "__main__":
    main()
