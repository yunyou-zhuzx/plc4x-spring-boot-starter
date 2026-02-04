package com.yunyoucloud.plc4x.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlcProtocol {
	
	//
	S7("s7"),
	OPCUA("opcua"),
	;
	
	private final String protocol;
}
