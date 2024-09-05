import rclpy
from rclpy.node import Node
from geometry_msgs.msg import Twist
from tf2_ros import TransformListener, Buffer
import numpy as np
from math import atan2, pi
from .submodules.myutil import clamp, PCA9685, PWMThrottleRacer, PWMThrottle2Wheel, PWMThrottleHat, PWMSteering
import subprocess

class RotateToTargetOrientation(Node):
    def __init__(self):
        super().__init__('rotate_to_target_orientation')

        # Publisher for cmd_vel
        self.cmd_vel_pub = self.create_publisher(Twist, 'cmd_vel', 10)

        # TF buffer and listener
        self.tf_buffer = Buffer()
        self.tf_listener = TransformListener(self.tf_buffer, self)

        # Target orientation (quaternion w = 0.7, z = 0.7)
        self.target_orientation = [0.0, 0.0, 0.7, 0.7]

        # Timer to call the control loop
        self.control_timer = self.create_timer(0.1, self.control_loop)
        self._right = PCA9685(channel=8, address=0x60, busnum=1, center=380)
        self._left = PCA9685(channel=4, address=0x60, busnum=1, center=380)
        self._center = PCA9685(channel=7, address=0x60, busnum=1, center=380)
        self.flag=0

    def control_loop(self):
        try:
            # Lookup the current transform from the odom frame to the base_footprint (or base_link)
            trans = self.tf_buffer.lookup_transform('odom', 'base_footprint', rclpy.time.Time())

            # Extract the current orientation as quaternion
            current_orientation = [
                trans.transform.rotation.x,
                trans.transform.rotation.y,
                trans.transform.rotation.z,
                trans.transform.rotation.w
            ]

            # Convert quaternions to Euler angles (yaw only)
            current_yaw = self.quaternion_to_yaw(current_orientation)
            target_yaw = self.quaternion_to_yaw(self.target_orientation)

            # Calculate the yaw difference
            yaw_error = target_yaw - current_yaw

            # Normalize yaw_error to the range [-pi, pi]
            if yaw_error > pi:
                yaw_error -= 2 * pi
            elif yaw_error < -pi:
                yaw_error += 2 * pi

            # Create Twist message
            twist = Twist()

            # Simple proportional controller for angular.z
            k_p = 1.0  # Proportional gain
            twist.angular.z = k_p * yaw_error

            # Set linear velocity to zero (just rotating in place)
            twist.linear.x = 0.0

            # Publish the cmd_vel message
            self.cmd_vel_pub.publish(twist)

            # Stop when within a small threshold
            if abs(yaw_error) < 0.01:
                twist.angular.z = 0.0
                self.cmd_vel_pub.publish(twist)
                self.get_logger().info('Reached the target orientation')
                
                # 노드를 종료
                self.get_logger().info('Shutting down node...')
                command = ["ros2", "run", "monicar_control", "chase_traffic_yolo"]
                result = subprocess.run(command, capture_output=True, text=True)
                print(1)
                rclpy.shutdown()

        except Exception as e:
            self.get_logger().warn(f"Could not transform: {e}")

    def quaternion_to_yaw(self, quaternion):
        """Convert a quaternion into a yaw angle (in radians)."""
        x, y, z, w = quaternion
        siny_cosp = 2 * (w * z + x * y)
        cosy_cosp = 1 - 2 * (y * y + z * z)
        return atan2(siny_cosp, cosy_cosp)

def main(args=None):
    rclpy.init(args=args)
    node = RotateToTargetOrientation()
    rclpy.spin(node)
    node.destroy_node()

if __name__ == '__main__':
    main()
