package com.yunyoucloud.plc4x.exception;

public class PlcWriteExpection extends RuntimeException {
	
	public PlcWriteExpection(String message) {
		super("write plc data exception: " +message);
	}
}
