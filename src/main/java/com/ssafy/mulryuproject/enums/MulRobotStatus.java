package com.ssafy.mulryuproject.enums;

public enum MulRobotStatus {
	REST(0), WORK(1);

	private final int value;

	MulRobotStatus(int value) {
	        this.value = value;
	    }

	public int getValue() {
		return value;
	}
	
	public MulRobotStatus toggle() {
        return this == REST ? WORK : REST;
    }
}
