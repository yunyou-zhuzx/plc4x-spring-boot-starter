package com.yunyoucloud.plc4x.client;

import com.yunyoucloud.plc4x.config.connection.ConnectionConfig;
import com.yunyoucloud.plc4x.core.PlcParseData;
import com.yunyoucloud.plc4x.core.RequestItem;
import com.yunyoucloud.plc4x.core.ResponseItem;
import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import com.yunyoucloud.plc4x.exception.PlcReadExpection;
import com.yunyoucloud.plc4x.exception.PlcWriteExpection;
import com.yunyoucloud.plc4x.resolve.PlcResolve;
import com.yunyoucloud.plc4x.utils.ByteUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.DefaultPlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.messages.PlcWriteRequest;
import org.apache.plc4x.java.api.messages.PlcWriteResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Getter
@SuppressWarnings("unchecked")
public class PLC {
	
	private PlcConnection plcConnection;
	private final PlcProtocol plcProtocol;
	private final ConnectionConfig connectionConfig;
	private boolean isConnected = false;
	
	// 定义需要判断的包装类型集合
	private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Set.of(
		Integer.class, Boolean.class, Byte.class, Short.class,
		Long.class, Float.class, Double.class, Character.class
	));
	
	@SneakyThrows
	public PLC(final ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		this.plcConnection = createNewConnection();
		this.plcProtocol = connectionConfig.protocol();
	}
	
	public boolean checkAndReconnectConnection() {
//		if (Objects.nonNull(this.plcConnection)) {
//			if (!this.plcConnection.isConnected()) {
//				try {
//					this.plcConnection.connect();
//				} catch (Exception e) {
//					log.error("reconnect to plc error: {}", e.getMessage(), e);
//					return false;
//				}
//			} else {
//				return true;
//			}
//		}
		
		// 二次确认，还是未连接，销毁重新新建
		if (Objects.nonNull(this.plcConnection) && this.isConnected) {
			return true;
		}
		
		// 连接断开，尝试重连
		destroyStaleConnection();
		return reconnectWithRetry();
	}
	
	/**
	 * 重试重连机制
	 */
	public boolean reconnectWithRetry() {
		final int maxRetries = 3;
		final long retryDelayMs = 1000;
		
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				this.plcConnection = createNewConnection();
				if (Objects.nonNull(this.plcConnection) && this.isConnected) {
					log.info("PLC connection reestablished successfully (attempt {}/{})", attempt, maxRetries);
					return true;
				}
				// 连接失败，销毁旧连接
				else {
					destroyStaleConnection();
				}
			} catch (Exception e) {
				this.isConnected = false;
				log.warn("PLC reconnection attempt {} failed: {}", attempt, e.getMessage());
			}
			
			if (attempt < maxRetries) {
				try {
					Thread.sleep(retryDelayMs);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
		
		log.error("Failed to reconnect to PLC after {} attempts", maxRetries);
		return false;
	}
	
	/**
	 * 销毁旧的连接对象
	 */
	public void destroyStaleConnection() {
		if (Objects.nonNull(this.plcConnection)) {
			try {
				if (this.isConnected) {
					this.plcConnection.close();
				}
			} catch (Exception e) {
				log.warn("Error closing stale PLC connection: {}", e.getMessage(), e);
			} finally {
				this.isConnected = false;
				this.plcConnection = null;
			}
		}
	}
	
	/**
	 * 创建新的连接
	 */
	public PlcConnection createNewConnection() throws PlcConnectionException {
		try {
			PlcConnection plcConnection = new DefaultPlcDriverManager().getConnection(this.connectionConfig.address());
			isConnected = true;
			return plcConnection;
		} catch (Exception e) {
			isConnected = false;
			throw new PlcConnectionException("Failed to create new plc connection" + e.getMessage(), e);
		}
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
	public synchronized <T> T read(final String tagName, final String address, final Class<T> returnClass) {
		if (!checkAndReconnectConnection()) {
			log.error("Unable to establish PLC connection, plc address: {}", connectionConfig.address());
			throw new PlcConnectionException("Unable to establish PLC connection");
		}
		
		if (!plcConnection.getMetadata().isReadSupported()) {
			log.error("This connection doesn't support reading, plc address: {}", connectionConfig.address());
			throw new PlcReadExpection("This connection doesn't support reading.");
		}
		
		try {
			final PlcReadRequest.Builder requestBuilder = this.plcConnection.readRequestBuilder();
			requestBuilder.addTagAddress(tagName, address);
			final PlcReadRequest plcReadRequest = requestBuilder.build();
			final PlcReadResponse plcReadResponse = plcReadRequest.execute().get(10, TimeUnit.SECONDS);
			final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
			if (responseCode == PlcResponseCode.OK) {
				return resolvePlcValue(tagName, plcReadResponse, returnClass);
			} else {
				log.warn("read address {} failed, tagName = {}, reason = {}", address, tagName, responseCode.name());
				return null;
			}
		} catch (TimeoutException e) {
			destroyStaleConnection();
			throw new RuntimeException(e);
		}
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
	public synchronized void read(final List<PlcParseData> plcParseDataList, final PlcResolve plcResolve) {
		if (!checkAndReconnectConnection()) {
			log.error("Unable to establish PLC connection, plc address: {}", connectionConfig.address());
			throw new PlcConnectionException("Unable to establish PLC connection");
		}
		
		if (!plcConnection.getMetadata().isReadSupported()) {
			log.error("This connection doesn't support reading, plc address: {}", connectionConfig.address());
			throw new PlcReadExpection("This connection doesn't support reading.");
		}
		
		try {
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
					final Object value = plcResolve.resolve(plcParseData, requestItem.getTagName(), plcReadResponse);
					plcParseData.getResponseItem().setValue(value);
				} else {
					log.error("读取PLC数据失败：tagName = {}, reason = {}", tagName, responseCode.name());
					plcParseData.getResponseItem().setValue(null);
				}
			}
		} catch (TimeoutException e) {
			destroyStaleConnection();
			throw new RuntimeException(e);
		}
	}
	
	public <T> void write(final String address, final T value) {
		write("tmpTag", address, value);
	}
	
	@SneakyThrows
	public synchronized <T> void write(final String tagName, final String address, final T value) {
		if (!checkAndReconnectConnection()) {
			log.error("Unable to establish PLC connection, plc address: {}", connectionConfig.address());
			throw new PlcConnectionException("Unable to establish PLC connection");
		}
		
		if (!plcConnection.getMetadata().isWriteSupported()) {
			log.error("This connection doesn't support writing, plc address: {}", connectionConfig.address());
			throw new PlcWriteExpection("This connection doesn't support writing.");
		}
		
		try {
			final PlcWriteRequest.Builder writeRequestBuilder = this.plcConnection.writeRequestBuilder();
			writeRequestBuilder.addTagAddress(tagName, address, value);
			final PlcWriteRequest plcWriteRequest = writeRequestBuilder.build();
			final PlcWriteResponse plcReadResponse = plcWriteRequest.execute().get();
			final PlcResponseCode responseCode = plcReadResponse.getResponseCode(tagName);
			if (responseCode != PlcResponseCode.OK) {
				log.warn("write address {} failed, tagName = {}, reason = {}", address, tagName, responseCode.name());
				throw new PlcWriteExpection(String.format("tagName = %s, reason = %s", tagName, responseCode.name()));
			}
		} catch (ExecutionException | PlcWriteExpection e) {
			destroyStaleConnection();
			throw new RuntimeException(e);
		}
	}
	
	@SneakyThrows
	public synchronized void write(final List<PlcParseData> plcParseDataList) {
		if (!checkAndReconnectConnection()) {
			log.error("Unable to establish PLC connection, plc address: {}", connectionConfig.address());
			throw new PlcConnectionException("Unable to establish PLC connection");
		}
		
		if (!plcConnection.getMetadata().isWriteSupported()) {
			log.error("This connection doesn't support writing, plc address: {}", connectionConfig.address());
			throw new PlcWriteExpection("This connection doesn't support writing.");
		}
		
		try {
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
		} catch (ExecutionException | PlcWriteExpection e) {
			destroyStaleConnection();
			throw new RuntimeException(e);
		}
	}
	
	public <T> T resolvePlcValue(final String tagName, final PlcReadResponse plcReadResponse, final Class<T> returnClass) {
		if (WRAPPER_TYPES.contains(returnClass)) {
			if (returnClass == Boolean.class) {
				return (T) plcReadResponse.getBoolean(tagName);
			}
			if (returnClass == Byte.class) {
				return (T) plcReadResponse.getByte(tagName);
			}
			if (returnClass == Character.class) {
				return (T) plcReadResponse.getByte(tagName);
			}
			if (returnClass == Short.class) {
				return (T) plcReadResponse.getShort(tagName);
			}
			if (returnClass == Integer.class) {
				return (T) plcReadResponse.getInteger(tagName);
			}
			if (returnClass == Long.class) {
				return (T) plcReadResponse.getLong(tagName);
			}
			if (returnClass == Float.class) {
				return (T) plcReadResponse.getFloat(tagName);
			}
			if (returnClass == Double.class) {
				return (T) plcReadResponse.getDouble(tagName);
			}
		}
		return resolveCustomValue(tagName, plcReadResponse, returnClass);
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
