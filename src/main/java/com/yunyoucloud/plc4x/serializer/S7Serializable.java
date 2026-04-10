package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.annotations.PlcVariable;

public class S7Serializable extends AbstractPlcSerializer {
	
	public S7Serializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, PlcVariable plcVariable) {
		final String address = plcVariable.address();
		final String[] split = address.split("\\.");
		String dbName = dbAddress.isBlank() ? split[0] : dbAddress;
		return "%" + dbName + ":" + split[1] + ":" + plcVariable.type().getCode();
	}
	
	public static S7Serializable newInstance(final PLC plc) {
		return new S7Serializable(plc);
	}
}
