package com.ssafy.mulryuproject.enums;

public enum MulRobotStatus {
	OFFLINE(0), ONLINE(1), ERROR(2);

	private final int value;

	MulRobotStatus(int value) {
	        this.value = value;
	    }

	public int getValue() {
		return value;
	}
}
