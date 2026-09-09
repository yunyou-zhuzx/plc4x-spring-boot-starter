package com.yunyoucloud.plc4x.utils;

import com.yunyoucloud.plc4x.exception.PlcReadExpection;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ByteUtils {
	
	public String byteToBinaryString(byte b) {
		return String.format("%8s", Integer.toBinaryString(b & 0xFF))
			.replace(' ', '0');
	}
	
	public String shortToString(short s) {
		String binS = String.format("%16s", Integer.toBinaryString(s & 0xFFFF))
			.replace(' ', '0');
		char c1 = (char) Integer.parseInt(binS.substring(0, 8), 2);
		char c2 = (char) Integer.parseInt(binS.substring(8, 16), 2);
		
		if (c1 == 0) {
			return "" + (c2 == 0 ? "" : c2);
		}
		
		return c1 + String.valueOf((c2 == 0 ? "" : c2));
	}
	
	/**
	 * 将字符串每两个字符转换为一个 short（ASCII）
	 *
	 * @param str 输入字符串
	 * @return short 集合
	 */
	public static List<Short> stringToShorts(String str) {
		if (str == null || str.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<Short> result = new ArrayList<>();
		
		for (int i = 0; i + 1 < str.length(); i += 2) {
			char highChar = str.charAt(i);
			char lowChar = str.charAt(i + 1);
			
			short value = (short) ((highChar << 8) | lowChar);
			result.add(value);
		}
		
		return result;
	}
	
	public int getBit(int value, int bitIndex, ByteOrder order) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(order).putShort((short) value);
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			res.insert(0, byteToBinaryString(buffer.get(i)));
		}
		int index = res.length() - bitIndex - 1;
		return Integer.parseInt(res.substring(index, index + 1));
	}
	
	public int getBits(final int value, final String bits, final ByteOrder order) {
		if (StringUtils.isBlank(bits)) {
			throw new PlcReadExpection("bits cannot be blank");
		}
		final String[] split = bits.split(":");
		if (split.length > 2) {
			throw new PlcReadExpection("bits is invalid");
		}
		
		boolean isLen = split[0].trim().startsWith("[") && split[0].trim().endsWith("]");
		int len = isLen ? Integer.parseInt(split[0].substring(1, split[0].length() - 1)) : 1;
		int start = isLen ? 0 : Integer.parseInt(split[0]);
		int end = (split.length == 2 ? Integer.parseInt(split[1].substring(1, split[1].length() - 1)) - 1 : isLen ? len - 1 : 0) + start;
		
		if (start < 0 || start > 15 || end < 0 || end > 15) {
			throw new PlcReadExpection("bits is invalid");
		}
		
		StringBuilder res = new StringBuilder();
		for (int i = start; i <= end; i++) {
			res.insert(0, ByteUtils.getBit(value, i, order));
		}
		return Integer.parseInt(res.toString(), 2);
	}
	
	/**
	 * 设置 short 中指定 bit 位的值
	 *
	 * @param value  原始 short 值
	 * @param bitPos bit 位位置（0~15，0 表示最低位）
	 * @param bitVal 要设置的值（true=1，false=0）
	 * @return 设置后的 short 值
	 */
	public int setBit(int value, int bitPos, boolean bitVal) {
		if (bitPos < 0 || bitPos > 15) {
			throw new IllegalArgumentException("bitPos 必须在 0~15 之间");
		}
		
		if (bitVal) {
			// 置 1
			return (value | (1 << bitPos));
		} else {
			// 置 0
			return (value & ~(1 << bitPos));
		}
	}
	
	/**
	 * 设置 short 中一段连续 bit 位的值
	 *
	 * @param value     原始 short
	 * @param startBit  起始位（0~15）
	 * @param bitLength 位长度（1~16）
	 * @param newValue  要设置的新值
	 * @return 修改后的 short
	 */
	public int setBits(int value, int startBit, int bitLength, int newValue) {
		if (startBit < 0 || startBit > 15) {
			throw new IllegalArgumentException("startBit 必须在 0~15 之间");
		}
		if (bitLength <= 0 || bitLength > 16) {
			throw new IllegalArgumentException("bitLength 必须在 1~16 之间");
		}
		if (startBit + bitLength > 16) {
			throw new IllegalArgumentException("startBit + bitLength 不能超过 16");
		}
		
		// 1. 生成掩码（对应长度的 1）
		int mask = (1 << bitLength) - 1;
		
		// 2. 检查 newValue 是否越界
		if ((newValue & ~mask) != 0) {
			throw new IllegalArgumentException("newValue 超出可表示范围");
		}
		
		// 3. 清除原字段
		value = (value & ~(mask << startBit));
		
		// 4. 写入新值
		value = (value | (newValue << startBit));
		
		return value;
	}
	
	public int setBits(final int value, final String bits, int newValue) {
		if (StringUtils.isBlank(bits)) {
			throw new PlcReadExpection("bits cannot be blank");
		}
		final String[] split = bits.split(":");
		if (split.length > 2) {
			throw new PlcReadExpection("bits is invalid");
		}
		
		boolean isLen = split[0].trim().startsWith("[") && split[0].trim().endsWith("]");
		int len = isLen ? Integer.parseInt(split[0].substring(1, split[0].length() - 1)) : 1;
		int start = isLen ? 0 : Integer.parseInt(split[0]);
		
		if (start < 0 || start > 15) {
			throw new PlcReadExpection("bits is invalid");
		}
		
		return setBits(value, start, len, newValue);
	}
	
	public int combineShortToInteger(short high, short low) {
		return ((high & 0xFFFF) << 16) | (low & 0xFFFF);
	}
	
	/**
	 * 从 int 中获取高 16 位 short
	 */
	public static short getHighShort(int value) {
		return (short) (value >>> 16);
	}
	
	/**
	 * 从 int 中获取低 16 位 short
	 */
	public static short getLowShort(int value) {
		return (short) (value & 0xFFFF);
	}
}
