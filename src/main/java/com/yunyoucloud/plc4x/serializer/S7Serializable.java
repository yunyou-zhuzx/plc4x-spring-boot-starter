package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;

public class S7Serializable extends AbstractPlcSerializer {
	
	public S7Serializable(final PLC plc) {
		super(plc);
	}
	
	public static S7Serializable newInstance(final PLC plc) {
		return new S7Serializable(plc);
	}
}
