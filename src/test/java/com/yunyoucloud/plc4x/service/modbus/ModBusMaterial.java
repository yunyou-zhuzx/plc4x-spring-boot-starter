package com.yunyoucloud.plc4x.service.modbus;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import lombok.Data;

@Data
public class ModBusMaterial {
	
	@PlcVariable(type = EDataType.INT16, address = "holding-register:1")
	private Short status;
	
	@PlcVariable(type = EDataType.BOOL, address = "coil:1")
	private Boolean hasCoils;
	
}
