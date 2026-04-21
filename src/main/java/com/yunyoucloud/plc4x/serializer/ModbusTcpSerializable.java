package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.enums.EDataType;

public class ModbusTcpSerializable extends AbstractClientPlcSerializer {
	
	public ModbusTcpSerializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, String address, EDataType dataType) {
		return dbAddress + address;
	}
	
	public static ModbusTcpSerializable newInstance(final PLC plc) {
		return new ModbusTcpSerializable(plc);
	}
}
