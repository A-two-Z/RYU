package com.ssafy.mulryuproject.enums;

public enum MulOrderStatus {
	REST(0), WORK(1);

	private final int value;

	MulOrderStatus(int value) {
	        this.value = value;
	    }

	public int getValue() {
		return value;
	}
}
