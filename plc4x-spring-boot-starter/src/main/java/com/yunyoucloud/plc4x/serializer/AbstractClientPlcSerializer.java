package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.PlcParseData;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.exception.PlcCommExpection;
import com.yunyoucloud.plc4x.resolve.PlcResolve;
import lombok.SneakyThrows;
import org.apache.plc4x.java.api.messages.PlcReadResponse;

public abstract class AbstractClientPlcSerializer extends AbstractPlcSerializer {
	
	public AbstractClientPlcSerializer(final PLC plc) {
		super(plc);
	}
	
	@Override
	public PlcResolve getPlcResolve() {
		return new PlcResolve() {
			@Override
			public Object resolve(final PlcParseData plcParseData, final String tagName, final PlcReadResponse plcReadResponse) {
				return plc.resolvePlcValue(tagName, plcReadResponse, plcParseData.getDataType().getClazz());
			}
			
			@Override
			@SneakyThrows
			public Object extract(final Object targetFiledValueDb, final PlcParseData plcParseData) {
				return targetFiledValueDb;
			}
		};
	}
	
	@Override
	public boolean readBoolean(String address) {
		return singleRead(address, Boolean.class, EDataType.BOOL);
	}
	
	@Override
	public byte readByte(String address) {
		return singleRead(address, Byte.class, EDataType.BYTE);
	}
	
	@Override
	public short readShort(String address) {
		return singleRead(address, Short.class, EDataType.INT16);
	}
	
	@Override
	public int readInteger(String address) {
		return singleRead(address, Integer.class, EDataType.INT32);
	}
	
	@Override
	public long readLong(String address) {
		return singleRead(address, Long.class, EDataType.INT64);
	}
	
	@Override
	public float readFloat(String address) {
		return singleRead(address, Float.class, EDataType.FLOAT32);
	}
	
	@Override
	public double readDouble(String address) {
		return singleRead(address, Double.class, EDataType.FLOAT64);
	}
	
	@Override
	public String readString(String address) {
		return singleRead(address, String.class, EDataType.STRING);
	}
	
	@Override
	public void writeBoolean(String address, boolean value) {
		singleWrite(address, value, EDataType.BOOL);
	}
	
	@Override
	public void writeByte(String address, byte value) {
		singleWrite(address, value, EDataType.BYTE);
	}
	
	@Override
	public void writeShort(String address, short value) {
		singleWrite(address, value, EDataType.INT16);
	}
	
	@Override
	public void writeInteger(String address, int value) {
		singleWrite(address, value, EDataType.INT32);
	}
	
	@Override
	public void writeLong(String address, long value) {
		singleWrite(address, value, EDataType.INT64);
	}
	
	@Override
	public void writeFloat(String address, float value) {
		singleWrite(address, value, EDataType.FLOAT32);
	}
	
	@Override
	public void writeDouble(String address, double value) {
		singleWrite(address, value, EDataType.FLOAT64);
	}
	
	@Override
	public void writeString(String address, String value) {
		singleWrite(address, value, EDataType.STRING);
	}
	
	public <T> T singleRead(final String address, final Class<T> tClass, final EDataType eDataType) {
		if (eDataType == null) {
			throw new PlcCommExpection("类型不正确");
		}
		return plc.read(resolveAddress("", address, eDataType), tClass);
	}
	
	public <T> void singleWrite(final String address, final T value, final EDataType eDataType) {
		if (eDataType == null) {
			throw new PlcCommExpection("类型不正确");
		}
		final PlcParseData plcParseData = new PlcParseData();
		plcParseData.setDataType(eDataType);
		plcParseData.setBits(resolveBits(address));
		plcParseData.getRequestItem().setAddress(address);
		plc.write(resolveAddress("", address, eDataType), getPlcResolve().extract(value, plcParseData));
	}
	
}
