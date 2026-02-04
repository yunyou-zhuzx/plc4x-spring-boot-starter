package com.yunyoucloud.plc4x.config;

import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import com.yunyoucloud.plc4x.config.connection.ConnectionConfig;

import java.util.Map;

public interface IPlcConfig {
	
	/**
	 * 是否启用
	 *
	 * @return 是否启用
	 */
	boolean isEnable();
	
	/**
	 * 连接配置
	 *
	 * @return 连接配置
	 */
	Map<String, ? extends ConnectionConfig> getConnections();
	
	/**
	 * 协议
	 *
	 * @return 协议
	 */
	PlcProtocol protocol();
}
