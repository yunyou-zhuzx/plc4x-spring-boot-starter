package com.yunyoucloud.plc4x.serializer;

import cn.hutool.core.util.StrUtil;
import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.exception.PlcCommExpection;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S7Serializable extends AbstractClientPlcSerializer {
	
	public S7Serializable(final PLC plc) {
		super(plc);
	}
	
	@Override
	public String resolveAddress(String dbAddress, String address, EDataType dataType) {
		if (!validateDbAddress(address)) {
			throw new PlcCommExpection("Invalid address format: " + address);
		}
		final int index = address.indexOf(".");
		final String dbName = dbAddress.isBlank() ? address.substring(0, index) : dbAddress;
		final String dbSuffix = address.substring(index + 1);
		final String[] strings = extractDbSuffix(dbSuffix);
		String countStr = StrUtil.isBlank(strings[1]) ? "" : "(" + strings[1] + ")";
		return "%" + dbName + ":" + strings[0] + ":" + dataType.getCode() + countStr;
	}
	
	public static boolean validateDbAddress(String address) {
		String regex = "^DB\\d+\\.\\d+(?:\\.\\d+|\\[\\d+\\]|\\.\\d+\\[\\d+\\])?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
	
	public static String[] extractDbSuffix(String dbSuffix) {
		String regex = "^([\\d.]+)(?:\\[(\\d+)\\])?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dbSuffix);
		
		if (matcher.matches()) {
			String part1 = matcher.group(1);
			String part2 = Objects.isNull(matcher.group(2)) ? "" : matcher.group(2);
			return new String[]{part1, part2};
		} else {
			throw new IllegalArgumentException("Invalid address format: " + dbSuffix);
		}
	}
	
	public static S7Serializable newInstance(final PLC plc) {
		return new S7Serializable(plc);
	}
}
