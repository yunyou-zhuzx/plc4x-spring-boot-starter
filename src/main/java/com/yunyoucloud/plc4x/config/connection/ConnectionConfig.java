package com.yunyoucloud.plc4x.config.connection;

import com.yunyoucloud.plc4x.common.enums.PlcProtocol;

public interface ConnectionConfig {
	
	/**
	 * 是否启用
	 * @return 是否启用
	 */
	boolean enable();
	
	/**
	 * 连接地址
	 *
	 * @return 连接地址
	 */
	String address();
	
	/**
	 * 协议
	 *
	 * @return 协议
	 */
	PlcProtocol protocol();
	
}
