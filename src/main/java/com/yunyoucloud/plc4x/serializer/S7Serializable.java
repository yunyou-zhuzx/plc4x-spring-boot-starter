package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.enums.EDataType;

public class S7Serializable extends AbstractClientPlcSerializer {
	
	public S7Serializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, String address, EDataType dataType) {
		final String[] split = address.split("\\.");
		String dbName = dbAddress.isBlank() ? split[0] : dbAddress;
		return "%" + dbName + ":" + split[1] + ":" + dataType.getCode();
	}
	
	public static S7Serializable newInstance(final PLC plc) {
		return new S7Serializable(plc);
	}
}
