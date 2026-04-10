package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.annotations.PlcVariable;

public class ModbusTcpSerializable extends AbstractPlcSerializer {
	
	public ModbusTcpSerializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, PlcVariable plcVariable) {
		return dbAddress + plcVariable.address();
	}
	
	public static ModbusTcpSerializable newInstance(final PLC plc) {
		return new ModbusTcpSerializable(plc);
	}
}
