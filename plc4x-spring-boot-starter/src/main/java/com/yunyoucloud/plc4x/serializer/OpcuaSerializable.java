package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.enums.EDataType;

public class OpcuaSerializable extends AbstractClientPlcSerializer {
	
	public OpcuaSerializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, String address, EDataType dataType) {
		return dbAddress + address;
	}
	
	public static OpcuaSerializable newInstance(final PLC plc) {
		return new OpcuaSerializable(plc);
	}
}
