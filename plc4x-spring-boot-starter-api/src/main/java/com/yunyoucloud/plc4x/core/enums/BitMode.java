package com.yunyoucloud.plc4x.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteOrder;

@Getter
@RequiredArgsConstructor
public enum BitMode {
	//大端模式
	HEIGHT(ByteOrder.BIG_ENDIAN),
	//小端模式
	LOW(ByteOrder.LITTLE_ENDIAN);
	
	private final ByteOrder byteOrder;
}
