package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.exception.PlcCommExpection;

public interface IPLCSerializable {
	
	/**
	 * 读取数据
	 *
	 * @param db DB块
	 * @return 数据
	 */
	<T> T read(Class<T> db);
	
	/**
	 * 读取数据
	 *
	 * @param db    DB块
	 * @param index 索引
	 * @return 数据
	 */
	<T> T read(Class<T> db, Integer index);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	boolean readBoolean(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	byte readByte(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	short readShort(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	int readInteger(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	long readLong(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	float readFloat(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	double readDouble(String address);
	
	/**
	 * 读取数据
	 *
	 * @param address 地址
	 * @return 数据
	 */
	String readString(String address);
	
	/**
	 * 写入数据
	 *
	 * @param db DB块
	 */
	<T> void write(T db);
	
	/**
	 * 写入数据
	 *
	 * @param db    DB块
	 * @param index 索引
	 */
	<T> void write(T db, Integer index);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeBoolean(String address, boolean value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeByte(String address, byte value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeShort(String address, short value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeInteger(String address, int value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeLong(String address, long value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeFloat(String address, float value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeDouble(String address, double value);
	
	/**
	 * 写入数据
	 *
	 * @param address 地址
	 * @param value   值
	 */
	void writeString(String address, String value);
	
	/**
	 * 创建PLC序列化器
	 *
	 * @param plc PLC
	 * @return PLC序列化器
	 */
	static IPLCSerializable newInstance(final PLC plc) {
		return switch (plc.getPlcProtocol()) {
			case OPCUA -> new OpcuaSerializable(plc);
			case S7 -> new S7Serializable(plc);
			case MODBUS_TCP -> new ModbusTcpSerializable(plc);
			default -> throw new PlcCommExpection("不支持的协议");
		};
	}
	
}
