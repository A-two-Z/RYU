#!/bin/bash

if [ "$#" -lt 1 ]; then
	echo "Usage: $0 target"
	echo "target: select one among these"
	echo "jetracer, jetbot, motorhat2wheel, motorhatSteer, nuriBdc, 298n2Wheel, pca9685Steer"
	exit 1
fi

cp ../monicar_control/param/motor.$1.yaml ../monicar_control/param/motor.yaml
