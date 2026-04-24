package com.yunyoucloud.plc4x.client;

import com.yunyoucloud.plc4x.core.PlcParseData;
import com.yunyoucloud.plc4x.core.RequestItem;
import com.yunyoucloud.plc4x.core.ResponseItem;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import com.yunyoucloud.plc4x.exception.PlcReadExpection;
import com.yunyoucloud.plc4x.exception.PlcWriteExpection;
import com.yunyoucloud.plc4x.utils.ByteUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.messages.PlcWriteRequest;
import org.apache.plc4x.java.api.messages.PlcWriteResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@SuppressWarnings("unchecked")
public class PLC {
	
	private final PlcConnection plcConnection;
	private final PlcProtocol plcProtocol;
	
	// 定义需要判断的包装类型集合
	private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Set.of(
		Integer.class, Boolean.class, Byte.class, Short.class,
		Long.class, Float.class, Double.class, Character.class
	));
	
	public PLC(
		final PlcConnection plcConnection,
		final PlcProtocol plcProtocol
	) {
		this.plcConnection = plcConnection;
		this.plcProtocol = plcProtocol;
	}
	
	public boolean readBoolean(String address) {
		return read(address, Boolean.class);
	}
	
	public byte readByte(String address) {
		return read(address, Byte.class);
	}
	
	public short readShort(String address) {
		return read(address, Short.class);
	}
	
	public int readInteger(String address) {
		return read(address, Integer.class);
	}
	
	public long readLong(String address) {
		return read(address, Long.class);
	}
	
	public float readFloat(String address) {
		return read(address, Float.class);
	}
	
	public double readDouble(String address) {
		return read(address, Double.class);
	}
	
	public String readString(String address) {
		return read(address, String.class);
	}
	
	public void writeBoolean(String address, boolean value) {
		write(address, value);
	}
	
	public void writeByte(String address, byte value) {
		write(address, value);
	}
	
	public void writeShort(String address, short value) {
		write(address, value);
	}
	
	public void writeInteger(String address, int value) {
		write(address, value);
	}
	
	public void writeLong(String address, long value) {
		write(address, value);
	}
	
	public void writeFloat(String address, float value) {
		write(address, value);
	}
	
	public void writeDouble(String address, double value) {
		write(address, value);
	}
	
	public void writeString(String address, String value) {
		write(address, value);
	}
	
	public <T> T read(final String address, final Class<T> returnClass) {
		return read("tmpTag", address, returnClass);
	}
	
	@SneakyThrows
	public <T> T read(final String tagName, final String address, final Class<T> returnClass) {
		final PlcReadRequest.Builder requestBuilder = this.plcConnection.readRequestBuilder();
		requestBuilder.addTagAddress(tagName, address);
		final PlcReadRequest plcReadRequest = requestBuilder.build();
		final PlcReadResponse plcReadResponse = plcReadRequest.execute().get(10, TimeUnit.SECONDS);
		final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
		if (responseCode == PlcResponseCode.OK) {
			return resolvePlcValue(tagName, plcReadResponse, returnClass);
		}
		throw new PlcReadExpection(String.format("tagName = %s, reason = %s", tagName, responseCode.name()));
	}
	
	public void read(final PlcParseData plcParseData) {
		final Object value = read(
			plcParseData.getRequestItem().getTagName(),
			plcParseData.getRequestItem().getAddress(),
			plcParseData.getDataType().getClazz()
		);
		
		plcParseData.getResponseItem().setValue(value);
	}
	
	@SneakyThrows
	public void read(final List<PlcParseData> plcParseDataList) {
		if (!this.plcConnection.isConnected()) {
			this.plcConnection.connect();
		}
		if (!plcConnection.getMetadata().isReadSupported()) {
			log.error("This connection doesn't support reading.");
			return;
		}
		final PlcReadRequest.Builder requestBuilder = this.plcConnection.readRequestBuilder();
		
		final Map<String, PlcParseData> plcParseDataMap = new HashMap<>(8);
		for (PlcParseData plcParseData : plcParseDataList) {
			final RequestItem requestItem = plcParseData.getRequestItem();
			plcParseDataMap.put(requestItem.getTagName(), plcParseData);
			requestBuilder.addTagAddress(requestItem.getTagName(), requestItem.getAddress());
		}
		
		final PlcReadRequest plcReadRequest = requestBuilder.build();
		final PlcReadResponse plcReadResponse = plcReadRequest.execute().get(10, TimeUnit.SECONDS);
		
		for (String tagName : plcReadResponse.getTagNames()) {
			final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
			final PlcParseData plcParseData = plcParseDataMap.get(tagName);
			if (responseCode == PlcResponseCode.OK) {
				final RequestItem requestItem = plcParseData.getRequestItem();
				final Object value = resolvePlcValue(requestItem.getTagName(), plcReadResponse, plcParseData);
				plcParseData.getResponseItem().setValue(value);
			} else {
				log.error("读取PLC数据失败：tagName = {}, reason = {}", tagName, responseCode.name());
				plcParseData.getResponseItem().setValue(null);
			}
		}
	}
	
	public <T> void write(final String address, final T value) {
		write("tmpTag", address, value);
	}
	
	@SneakyThrows
	public <T> void write(final String tagName, final String address, final T value) {
		final PlcWriteRequest.Builder writeRequestBuilder = this.plcConnection.writeRequestBuilder();
		writeRequestBuilder.addTagAddress(tagName, address, value);
		final PlcWriteRequest plcWriteRequest = writeRequestBuilder.build();
		final PlcWriteResponse plcReadResponse = plcWriteRequest.execute().get();
		final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
		if (responseCode != PlcResponseCode.OK) {
			throw new PlcWriteExpection(String.format("tagName = %s, reason = %s", tagName, responseCode.name()));
		}
	}
	
	@SneakyThrows
	public void write(final List<PlcParseData> plcParseDataList) {
		final PlcWriteRequest.Builder writeRequestBuilder = this.plcConnection.writeRequestBuilder();
		
		for (PlcParseData plcParseData : plcParseDataList) {
			final RequestItem requestItem = plcParseData.getRequestItem();
			final ResponseItem responseItem = plcParseData.getResponseItem();
			writeRequestBuilder.addTagAddress(requestItem.getTagName(), requestItem.getAddress(), responseItem.getValue());
		}
		
		final PlcWriteRequest plcWriteRequest = writeRequestBuilder.build();
		final PlcWriteResponse plcWriteResponse = plcWriteRequest.execute().get(10, TimeUnit.SECONDS);
		
		for (String tagName : plcWriteResponse.getTagNames()) {
			final PlcResponseCode responseCode = plcWriteResponse.getResponseCode(tagName);
			if (responseCode != PlcResponseCode.OK) {
				log.error("写入PLC数据失败：tagName = {}, reason = {}", tagName, responseCode.name());
			}
		}
	}
	
	private Object resolvePlcValue(final String tagName, final PlcReadResponse plcReadResponse, final PlcParseData plcParseData) {
		if (plcParseData.isBit()) {
			final Collection<Short> allShorts = plcReadResponse.getAllShorts(tagName);
			if (allShorts.size() == 1) {
				final Short i = allShorts.stream().findFirst().orElse((short) 0);
				final int bitIntValue = ByteUtils.getBits(i.intValue(), plcParseData.getBits(), plcParseData.getBitMode().getByteOrder());
				return plcParseData.getBitType().convert(bitIntValue);
			}
			
			if (Objects.equals(plcParseData.getBitType(), EDataType.FLOAT32)) {
				final List<Short> list = allShorts.stream().toList();
				final int bitIntValue = ByteUtils.combineShortToInteger(list.get(0), list.get(1));
				return plcParseData.getBitType().convert(bitIntValue);
			}
			
			if (Objects.equals(plcParseData.getBitType(), EDataType.STRING)) {
				StringBuilder res = new StringBuilder();
				for (Short allShort : allShorts) {
					final int bitIntValue = ByteUtils.getBits(allShort.intValue(), plcParseData.getBits(), plcParseData.getBitMode().getByteOrder());
					res.append(ByteUtils.shortToString((short) bitIntValue));
				}
				return res.toString();
			}
		}
		return resolvePlcValue(tagName, plcReadResponse, plcParseData.getDataType().getClazz());
	}
	
	private <T> T resolvePlcValue(final String tagName, final PlcReadResponse plcReadResponse, final Class<T> returnClass) {
		if (WRAPPER_TYPES.contains(returnClass)) {
			if (returnClass == Boolean.class) {
				final Collection<Boolean> allBooleans = plcReadResponse.getAllBooleans(tagName);
				if (allBooleans.size() > 1) {
					return resolveMultipartPlcValue(allBooleans, returnClass);
				}
				return (T) plcReadResponse.getBoolean(tagName);
			}
			if (returnClass == Byte.class) {
				final Collection<Byte> allBytes = plcReadResponse.getAllBytes(tagName);
				if (allBytes.size() > 1) {
					return resolveMultipartPlcValue(allBytes, returnClass);
				}
				return (T) plcReadResponse.getByte(tagName);
			}
			if (returnClass == Character.class) {
				final Collection<Byte> allBytes = plcReadResponse.getAllBytes(tagName);
				if (allBytes.size() > 1) {
					return resolveMultipartPlcValue(allBytes, returnClass);
				}
				return (T) plcReadResponse.getByte(tagName);
			}
			if (returnClass == Short.class) {
				final Collection<Short> allShorts = plcReadResponse.getAllShorts(tagName);
				if (allShorts.size() > 1) {
					return resolveMultipartPlcValue(allShorts, returnClass);
				}
				return (T) plcReadResponse.getShort(tagName);
			}
			if (returnClass == Integer.class) {
				final Collection<Integer> allIntegers = plcReadResponse.getAllIntegers(tagName);
				if (allIntegers.size() > 1) {
					return resolveMultipartPlcValue(allIntegers, returnClass);
				}
				return (T) plcReadResponse.getInteger(tagName);
			}
			if (returnClass == Long.class) {
				final Collection<Long> allLongs = plcReadResponse.getAllLongs(tagName);
				if (allLongs.size() > 1) {
					return resolveMultipartPlcValue(allLongs, returnClass);
				}
				return (T) plcReadResponse.getLong(tagName);
			}
			if (returnClass == Float.class) {
				final Collection<Float> allFloats = plcReadResponse.getAllFloats(tagName);
				if (allFloats.size() > 1) {
					return resolveMultipartPlcValue(allFloats, returnClass);
				}
				return (T) plcReadResponse.getFloat(tagName);
			}
			if (returnClass == Double.class) {
				final Collection<Double> allDoubles = plcReadResponse.getAllDoubles(tagName);
				if (allDoubles.size() > 1) {
					return resolveMultipartPlcValue(allDoubles, returnClass);
				}
				return (T) plcReadResponse.getDouble(tagName);
			}
		}
		return resolveCustomValue(tagName, plcReadResponse, returnClass);
	}
	
	private <T> T resolveMultipartPlcValue(Collection<?> valueList, Class<T> returnClass) {
		return (T) valueList.stream().findFirst().orElse(null);
	}
	
	private <T> T resolveCustomValue(final String address, final PlcReadResponse plcReadResponse, final Class<T> returnClass) {
		return null;
	}
	
	public static void main(String[] args) {
		int test = 12;
		final int i = ByteUtils.setBits(test, 3, 1, 0);
		System.out.println(Integer.toBinaryString(i));
		
		String testStr = "estun.main";
		for (Short stringToShort : ByteUtils.stringToShorts(testStr)) {
			System.out.println(Integer.toString(stringToShort, 16));
		}
		
		int test2 = 1073741824;
		System.out.println(Float.intBitsToFloat(test2));

//		System.out.println(ByteUtils.shortToString((short) getBits(29541, "[16]", ByteOrder.BIG_ENDIAN)));
	}
	
}
