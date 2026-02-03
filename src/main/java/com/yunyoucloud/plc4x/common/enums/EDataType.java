package com.yunyoucloud.plc4x.common.enums;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public enum EDataType {
	//
	BOOL("bool", 1, Boolean.class),
	BYTE("byte", 1, Byte.class),
	UINT16("uint16", 2, Short.class),
	INT16("int16", 2, Short.class),
	UINT32("uint32", 4, Integer.class),
	INT32("int32", 4, Integer.class),
	INT64("int64", 8, Long.class),
	FLOAT32("float32", 4, Float.class),
	FLOAT64("float64", 8, Double.class),
	STRING("string", 1, String.class),
	TIME("time", 4, Long.class),
	DATE("date", 2, LocalDate.class),
	TIME_OF_DAY("timeOfDay", 4, LocalTime.class),
	DTL("dtl", 12, LocalDateTime.class),
	OBJECT("object", 1, Object.class),
	LIST("list", 1, List.class),
	AUTO("auto", 1, Object.class);
	
	private static Map<String, EDataType> map;
	private final int byteLength;
	private final String name;
	private final Class<?> clazz;
	
	public static EDataType from(String data) {
		if (map == null) {
			map = new HashMap<>(2);
			
			for (EDataType item : values()) {
				map.put(item.name, item);
			}
		}
		
		return (EDataType) map.get(data);
	}
	
	private EDataType(String name, int byteLength, Class<?> clazz) {
		this.name = name;
		this.byteLength = byteLength;
		this.clazz = clazz;
	}
	
	public int getByteLength() {
		return this.byteLength;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Class<?> getClazz() {
		return this.clazz;
	}
}
