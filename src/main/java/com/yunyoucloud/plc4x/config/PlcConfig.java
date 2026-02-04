package com.yunyoucloud.plc4x.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.util.List;

@Data
@AutoConfiguration
@ConditionalOnBean(IPlcConfig.class)
public class PlcConfig {
	
	@Autowired
	private List<IPlcConfig> plcConfigs;
	
}

