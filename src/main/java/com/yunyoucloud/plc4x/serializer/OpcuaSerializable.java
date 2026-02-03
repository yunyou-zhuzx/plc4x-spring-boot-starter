package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;

public class OpcuaSerializable extends AbstractPlcSerializer {
	
	public OpcuaSerializable(final PLC plc) {
		super(plc);
	}
	
	public static OpcuaSerializable newInstance(final PLC plc) {
		return new OpcuaSerializable(plc);
	}
}
