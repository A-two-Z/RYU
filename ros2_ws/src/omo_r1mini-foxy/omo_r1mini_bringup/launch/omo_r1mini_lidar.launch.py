#!/usr/bin/python3
# Copyright 2020, EAIBOT
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from ament_index_python.packages import get_package_share_directory

from launch import LaunchDescription
from launch_ros.actions import LifecycleNode
from launch_ros.actions import Node
from launch.actions import DeclareLaunchArgument
from launch.substitutions import LaunchConfiguration
from launch.actions import LogInfo

import lifecycle_msgs.msg
import os


def generate_launch_description():

  channel_type =  LaunchConfiguration('channel_type', default='serial')
  serial_port = LaunchConfiguration('serial_port', default='/dev/ttyUSB0')
  serial_baudrate = LaunchConfiguration('serial_baudrate', default='115200')
  frame_id = LaunchConfiguration('frame_id', default='base_footprint')
  inverted = LaunchConfiguration('inverted', default='false')
  angle_compensate = LaunchConfiguration('angle_compensate', default='true')
  scan_mode = LaunchConfiguration('scan_mode', default='Sensitivity')

  omo_r1mini_lidar_parameter = LaunchConfiguration(
    'omo_r1mini_lidar_parameter',
    default=os.path.join(
      get_package_share_directory('omo_r1mini_bringup'),
      'param',
      'omo_r1mini_lidar.yaml'))

  return LaunchDescription([
    DeclareLaunchArgument(
      'omo_r1mini_lidar_parameter',
      default_value=omo_r1mini_lidar_parameter
    ),
    
    LifecycleNode(
      package='sllidar_ros2',
      executable='sllidar_node',
      name='sllidar_node',
      output='screen',
      emulate_tty=True,
      parameters=[{'channel_type':channel_type,
                         'serial_port': serial_port, 
                         'serial_baudrate': serial_baudrate, 
                         'frame_id': frame_id,
                         'inverted': inverted, 
                         'angle_compensate': angle_compensate}],
      namespace='/',
    )
  ])
