package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class ModbusTcpSerializable extends AbstractClientPlcSerializer {
	
	public ModbusTcpSerializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, String address, EDataType dataType) {
		return dbAddress + address;
	}
	
	@Override
	@SneakyThrows
	public <T> T read(final Class<T> db, final Integer index) {
		TimeUnit.SECONDS.sleep(1);
		return super.read(db, index);
	}
	
	public static ModbusTcpSerializable newInstance(final PLC plc) {
		return new ModbusTcpSerializable(plc);
	}
}
