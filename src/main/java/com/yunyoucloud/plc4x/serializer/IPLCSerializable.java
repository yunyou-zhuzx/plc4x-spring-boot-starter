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
	 * 创建PLC序列化器
	 *
	 * @param plc PLC
	 * @return PLC序列化器
	 */
	static IPLCSerializable newInstance(final PLC plc) {
		return switch (plc.getPlcProtocol()) {
			case OPCUA -> new OpcuaSerializable(plc);
			case S7 -> new S7Serializable(plc);
			default -> throw new PlcCommExpection("不支持的协议");
		};
	}
	
}
