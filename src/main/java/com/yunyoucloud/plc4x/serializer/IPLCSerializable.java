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
	
	boolean readBoolean(String address);
	
	byte readByte(String address);
	
	short readShort(String address);
	
	int readInteger(String address);
	
	long readLong(String address);
	
	float readFloat(String address);
	
	double readDouble(String address);
	
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
	
	void writeBoolean(String address, boolean value);
	
	void writeByte(String address, byte value);
	
	void writeShort(String address, short value);
	
	void writeInteger(String address, int value);
	
	void writeLong(String address, long value);
	
	void writeFloat(String address, float value);
	
	void writeDouble(String address, double value);
	
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
