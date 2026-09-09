package com.yunyoucloud.plc4x.config;

import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import com.yunyoucloud.plc4x.config.connection.S7ConnectionConfig;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;

import java.util.Map;

@Data
@Order(1)
@AutoConfiguration
@ConfigurationProperties(prefix = "plc.s7")
@ConditionalOnProperty(prefix = "plc.s7", name = "enable", havingValue = "true")
public class S7PlcConfig implements IPlcConfig {
	
	private boolean enable = false;
	
	private Map<String, S7ConnectionConfig> connections;
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.S7;
	}
}

