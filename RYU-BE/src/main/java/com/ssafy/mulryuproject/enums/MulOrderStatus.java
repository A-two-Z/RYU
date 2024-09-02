package com.ssafy.mulryuproject.enums;

public enum MulOrderStatus {
	WAIT(0), DELIVERY(1);

	private final int value;

	MulOrderStatus(int value) {
	        this.value = value;
	    }
	
	public String getValue() {
		return Integer.toString(value);
	}
	
}
