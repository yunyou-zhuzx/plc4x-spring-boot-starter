package com.yunyoucloud.plc4x.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Data
@Order(-10)
@AutoConfiguration
//@ConditionalOnBean(IPlcConfig.class)
public class PlcConfig {
	
	@Autowired
	private List<IPlcConfig> plcConfigs;
	
}

