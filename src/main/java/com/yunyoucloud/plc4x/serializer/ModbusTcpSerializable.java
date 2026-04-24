package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.PlcParseData;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.resolve.PlcResolve;
import com.yunyoucloud.plc4x.utils.ByteUtils;
import lombok.SneakyThrows;
import org.apache.plc4x.java.api.messages.PlcReadResponse;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ModbusTcpSerializable extends AbstractClientPlcSerializer {
	
	public ModbusTcpSerializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, String address, EDataType dataType) {
		final int index = address.lastIndexOf(".");
		if (index != -1) {
			return dbAddress + address.substring(0, index);
		}
		return dbAddress + address;
	}
	
	@Override
	public String resolveBits(String dbAddress) {
		final String[] split = dbAddress.split("\\.");
		if (split.length >= 2) {
			return split[split.length - 1];
		}
		return "[16]";
	}
	
	@Override
	@SneakyThrows
	public <T> T read(final Class<T> db, final Integer index) {
		TimeUnit.SECONDS.sleep(1);
		return super.read(db, index);
	}
	
	@Override
	public PlcResolve getPlcResolve() {
		return new PlcResolve() {
			@Override
			public Object resolve(final PlcParseData plcParseData, final String tagName, final PlcReadResponse plcReadResponse) {
				final List<Short> allShorts = plcReadResponse.getAllShorts(tagName).stream().toList();
				if (allShorts.size() == 1) {
					final int bitIntValue = ByteUtils.getBits(allShorts.get(0).intValue(), plcParseData.getBits(), plcParseData.getBitMode().getByteOrder());
					return convert((long) bitIntValue, plcParseData.getDataType().getClazz());
				}
				
				return convertList(allShorts, plcParseData.getDataType().getClazz());
			}
			
			@Override
			@SneakyThrows
			public Object extract(final Object targetFiledValueDb, final PlcParseData plcParseData) {
				if (Objects.nonNull(targetFiledValueDb)) {
					return reConvert(targetFiledValueDb, plcParseData);
				}
				return null;
			}
		};
	}
	
	private Object reConvert(final Object fieldValue, final PlcParseData plcParseData) {
		if (fieldValue instanceof String stringValue) {
			final List<Short> shorts = ByteUtils.stringToShorts(stringValue);
			if (shorts.size() < plcParseData.getCount()) {
				for (int i = 0; i < plcParseData.getCount() - shorts.size(); i++) {
					shorts.add((short) 0);
				}
			}
			return shorts;
		}
		if (fieldValue instanceof Float floatValue) {
			final int bitInt = Float.floatToIntBits(floatValue);
			return List.of(ByteUtils.getHighShort(bitInt), ByteUtils.getLowShort(bitInt));
		}
		if (fieldValue instanceof Boolean booleanValue) {
			final short shortValue = readShort(plcParseData.getRequestItem().getAddress());
			return ByteUtils.setBits(shortValue, plcParseData.getBits(), booleanValue ? 1 : 0);
		}
		if (fieldValue instanceof Short shortValue) {
			final short oldShortValue = readShort(plcParseData.getRequestItem().getAddress());
			return ByteUtils.setBits(oldShortValue, plcParseData.getBits(), shortValue);
		}
		return null;
	}
	
	private Object convertList(final List<Short> allShorts, final Class<?> clazz) {
		if (clazz == Float.class) {
			final int bitIntValue = ByteUtils.combineShortToInteger(allShorts.get(0), allShorts.get(1));
			return convert((long) bitIntValue, clazz);
		}
		
		if (clazz == String.class) {
			StringBuilder res = new StringBuilder();
			for (Short allShort : allShorts) {
				res.append(ByteUtils.shortToString(allShort));
			}
			return res.toString();
		}
		
		return null;
	}
	
	public Object convert(Long value, final Class<?> clazz) {
		if (clazz == String.class) {
			return String.valueOf(value);
		}
		if (clazz == Integer.class) {
			return value;
		}
		if (clazz == Long.class) {
			return value;
		}
		if (clazz == Double.class) {
			return Double.longBitsToDouble(value);
		}
		if (clazz == Boolean.class) {
			return value == 1;
		}
		if (clazz == Byte.class) {
			return value.byteValue();
		}
		if (clazz == Short.class) {
			return value.shortValue();
		}
		if (clazz == Float.class) {
			return Float.intBitsToFloat(value.intValue());
		}
		return clazz.cast(value);
	}
	
	public static ModbusTcpSerializable newInstance(final PLC plc) {
		return new ModbusTcpSerializable(plc);
	}
}
