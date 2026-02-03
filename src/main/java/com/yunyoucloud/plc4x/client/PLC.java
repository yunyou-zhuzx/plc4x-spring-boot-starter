package com.yunyoucloud.plc4x.client;

import com.yunyoucloud.plc4x.common.PlcParseData;
import com.yunyoucloud.plc4x.common.RequestItem;
import com.yunyoucloud.plc4x.common.ResponseItem;
import com.yunyoucloud.plc4x.common.enums.PlcProtocol;
import com.yunyoucloud.plc4x.exception.PlcReadExpection;
import com.yunyoucloud.plc4x.exception.PlcWriteExpection;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.messages.PlcWriteRequest;
import org.apache.plc4x.java.api.messages.PlcWriteResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		final PlcReadResponse plcReadResponse = plcReadRequest.execute().get();
		final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
		if (responseCode == PlcResponseCode.OK) {
			return resolvePlcValue(address, plcReadResponse, returnClass);
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
		final PlcReadRequest.Builder requestBuilder = this.plcConnection.readRequestBuilder();
		
		final Map<String, PlcParseData> plcParseDataMap = new HashMap<>(8);
		for (PlcParseData plcParseData : plcParseDataList) {
			final RequestItem requestItem = plcParseData.getRequestItem();
			plcParseDataMap.put(requestItem.getTagName(), plcParseData);
			requestBuilder.addTagAddress(requestItem.getTagName(), requestItem.getAddress());
		}
		
		final PlcReadRequest plcReadRequest = requestBuilder.build();
		final PlcReadResponse plcReadResponse = plcReadRequest.execute().get();
		
		for (String tagName : plcReadResponse.getTagNames()) {
			final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
			final PlcParseData plcParseData = plcParseDataMap.get(tagName);
			if (responseCode == PlcResponseCode.OK) {
				final RequestItem requestItem = plcParseData.getRequestItem();
				final Object value = resolvePlcValue(requestItem.getAddress(), plcReadResponse, plcParseData.getDataType().getClazz());
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
		final PlcWriteResponse plcWriteResponse = plcWriteRequest.execute().get();
		
		for (String tagName : plcWriteResponse.getTagNames()) {
			final PlcResponseCode responseCode = plcWriteResponse.getResponseCode(tagName);
			if (responseCode != PlcResponseCode.OK) {
				log.error("写入PLC数据失败：tagName = {}, reason = {}", tagName, responseCode.name());
			}
		}
	}
	
	private <T> T resolvePlcValue(final String address, final PlcReadResponse plcReadResponse, final Class<T> returnClass) {
		if (WRAPPER_TYPES.contains(returnClass)) {
			if (returnClass == Boolean.class) {
				return (T) plcReadResponse.getBoolean(address);
			}
			if (returnClass == Byte.class) {
				return (T) plcReadResponse.getByte(address);
			}
			if (returnClass == Character.class) {
				return (T) plcReadResponse.getByte(address);
			}
			if (returnClass == Short.class) {
				return (T) plcReadResponse.getShort(address);
			}
			if (returnClass == Integer.class) {
				return (T) plcReadResponse.getInteger(address);
			}
			if (returnClass == Long.class) {
				return (T) plcReadResponse.getLong(address);
			}
			if (returnClass == Float.class) {
				return (T) plcReadResponse.getFloat(address);
			}
			if (returnClass == Double.class) {
				return (T) plcReadResponse.getDouble(address);
			}
		}
		return resolveCustomValue(address, plcReadResponse, returnClass);
	}
	
	private <T> T resolveCustomValue(final String address, final PlcReadResponse plcReadResponse, final Class<T> returnClass) {
		return null;
	}
	
}
