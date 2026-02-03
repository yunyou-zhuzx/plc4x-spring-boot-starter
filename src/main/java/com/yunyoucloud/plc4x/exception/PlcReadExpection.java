package com.yunyoucloud.plc4x.exception;

public class PlcReadExpection extends RuntimeException {
	
	public PlcReadExpection(String message) {
		super("read plc data exception: " +message);
	}
}
