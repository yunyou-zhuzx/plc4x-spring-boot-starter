package com.yunyoucloud.plc4x.config;

import com.yunyoucloud.plc4x.config.connection.ModbusConnectionConfig;
import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;

import java.util.Map;

@Data
@Order(1)
@AutoConfiguration
@ConfigurationProperties(prefix = "plc.modbus")
@ConditionalOnProperty(prefix = "plc.modbus", name = "enable", havingValue = "true")
public class ModbusPLcConfig implements IPlcConfig {
	
	private boolean enable = false;
	
	private Map<String, ModbusConnectionConfig> connections;
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.S7;
	}
}

