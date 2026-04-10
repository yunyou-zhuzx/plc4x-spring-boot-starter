package com.yunyoucloud.plc4x.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum EDataType {
	//
	BOOL("bool", 1, Boolean.class, "BOOL"),
	BYTE("byte", 1, Byte.class, "BYTE"),
	UINT16("uint16", 2, Short.class, "UINT"),
	INT16("int16", 2, Short.class, "INT"),
	UINT32("uint32", 4, Integer.class, "UDINT"),
	INT32("int32", 4, Integer.class, "DINT"),
	INT64("int64", 8, Long.class, "LINT"),
	FLOAT32("float32", 4, Float.class, "REAL"),
	FLOAT64("float64", 8, Double.class, "LREAL"),
	STRING("string", 1, String.class, "STRING"),
	TIME("time", 4, Long.class, "TIME"),
	DATE("date", 2, LocalDate.class, "DATE"),
	TIME_OF_DAY("timeOfDay", 4, LocalTime.class, "TIME_OF_DAY"),
	DTL("dtl", 12, LocalDateTime.class, "BOOL"),
	OBJECT("object", 1, Object.class, "BOOL"),
	LIST("list", 1, List.class, "BOOL"),
	AUTO("auto", 1, Object.class, "BOOL");
	
	private static Map<String, EDataType> map;
	private final String name;
	private final int byteLength;
	private final Class<?> clazz;
	private final String code;
	
	public static EDataType from(String data) {
		if (map == null) {
			map = new HashMap<>(2);
			
			for (EDataType item : values()) {
				map.put(item.name, item);
			}
		}
		
		return (EDataType) map.get(data);
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
