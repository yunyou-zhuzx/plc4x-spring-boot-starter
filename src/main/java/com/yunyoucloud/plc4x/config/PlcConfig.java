package com.yunyoucloud.plc4x.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConditionalOnBean(IPlcConfig.class)
public class PlcConfig {
	
	@Autowired
	private List<IPlcConfig> plcConfigs;
	
}

