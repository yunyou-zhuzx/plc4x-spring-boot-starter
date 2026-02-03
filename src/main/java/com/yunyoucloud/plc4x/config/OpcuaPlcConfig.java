package com.yunyoucloud.plc4x.config;

import com.yunyoucloud.plc4x.common.enums.PlcProtocol;
import com.yunyoucloud.plc4x.config.connection.OpcuaConnectionConfig;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "plc.opcua")
@ConditionalOnProperty(prefix = "plc.opcua", name = "enable", havingValue = "true")
public class OpcuaPlcConfig implements IPlcConfig {
	
	private boolean enable = false;
	
	private Map<String, OpcuaConnectionConfig> connections;
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.OPCUA;
	}
}

