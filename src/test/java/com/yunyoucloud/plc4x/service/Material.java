package com.yunyoucloud.plc4x.service;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import lombok.Data;

@Data
public class Material {
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=DB1|status")
	private Short status;
	
	@PlcVariable(type = EDataType.FLOAT32, address = "ns=4;s=DB1|material.width")
	private Float width;
}
