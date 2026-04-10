package com.yunyoucloud.plc4x.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlcProtocol {
	
	//
	S7("s7"),
	OPCUA("opcua"),
	MODBUS_TCP("modbus-tcp"),
	;
	
	private final String protocol;
}
