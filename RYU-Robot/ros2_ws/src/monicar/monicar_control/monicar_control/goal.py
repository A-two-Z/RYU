#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import rclpy
from rclpy.node import Node
import time  
import RPi.GPIO as GPIO
from geometry_msgs.msg import Pose, Point, Quaternion
from std_msgs.msg import Int32
import pygame
from move_base_msgs.action import MoveBase

from rclpy.action import ActionClient

class MoveToDestinationsWithButton(Node):

    def __init__(self):
        super().__init__('move_to_destinations_with_button')
        self.client = ActionClient(self, MoveBase, 'move_base')

        self.destinations = [
            Pose(position=Point(x=1.6, y=0.0, z=0.0), orientation=Quaternion(x=0.0, y=0.0, z=0.0, w=1.0)),
            Pose(position=Point(x=1.6, y=1.8, z=0.0), orientation=Quaternion(x=0.0, y=0.0, z=0.7, w=0.7)),
            Pose(position=Point(x=0.0, y=1.7, z=0.0), orientation=Quaternion(x=0.0, y=0.0, z=1.0, w=0.0)),
            Pose(position=Point(x=0.0, y=0.0, z=0.0), orientation=Quaternion(x=0.0, y=0.0, z=0.0, w=1.0))
        ]
        self.extra_destination = Pose(position=Point(x=1.7, y=1.0, z=0.0), orientation=Quaternion(x=0.0, y=0.0, z=0.0, w=1.0))
        
        self.BUTTON = 20
        self.SENSOR_1 = 21
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.SENSOR_1, GPIO.IN)
        GPIO.setup(self.BUTTON, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

        self.red_light = 1
        self.blue_light = 2
        self.blink_blue = 3

        self.button_flag = 0
        self.pose = 0

        GPIO.add_event_detect(self.BUTTON, GPIO.RISING, callback=self.button_interrupt, bouncetime=1000)

        self.pub = self.create_publisher(Int32, 'my_topic', 10)
        
        self.timer = self.create_timer(0.1, self.main)

    def play_sound(self, sound_file):
        pygame.mixer.init()
        if not pygame.mixer.music.get_busy():
            pygame.mixer.music.load(sound_file)
            pygame.mixer.music.play()
    
    def stop_sound(self):
        pygame.mixer.music.stop()

    def send_light_msg(self, light_msg):
        msg = Int32()
        msg.data = light_msg
        for _ in range(2):
            self.pub.publish(msg)

    def button_interrupt(self, channel):
        self.get_logger().info("Button pressed")
        self.button_flag = 1
        time.sleep(0.5)

    def create_goal(self, destination):
        goal = MoveBase.Goal()
        goal.target_pose.header.frame_id = "map"
        goal.target_pose.header.stamp = self.get_clock().now().to_msg()
        goal.target_pose.pose = destination
        return goal

    def move_to_exit(self):
        self.send_light_msg(self.blue_light)
        self.stop_sound()
        goal = self.create_goal(self.extra_destination)
        self.client.send_goal(goal)
        while not rclpy.ok():
            self.play_sound("msg_tts2.wav")
            if self.client.get_result() == 2 or self.client.get_result() == 3:
                self.get_logger().info("Fire exit reached")
                self.send_light_msg(3)
                self.stop_sound()
                self.play_sound("msg_tts3.wav")
                time.sleep(5)
                self.button_flag = 0
                break

    def move_to_destination(self, destination):
        self.send_light_msg(self.red_light)
        goal = self.create_goal(destination)
        self.client.send_goal(goal)
        while not rclpy.ok():
            self.play_sound("msg_tts1.wav")
            if self.client.get_result() == 2 or self.client.get_result() == 3:
                break
            elif self.button_flag == 1:
                self.move_to_exit()
                self.button_flag = 0
                break

    def main(self):
        self.get_logger().info(f"Pose{self.pose} start")
        self.move_to_destination(self.destinations[self.pose])
        self.get_logger().info(f"Pose{self.pose} reached")
        self.pose = (self.pose + 1) % len(self.destinations)

def main(args=None):
    rclpy.init(args=args)
    node = MoveToDestinationsWithButton()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        pass
    finally:
        GPIO.cleanup()
        node.destroy_node()
        rclpy.shutdown()

if __name__ == '__main__':
    main()

