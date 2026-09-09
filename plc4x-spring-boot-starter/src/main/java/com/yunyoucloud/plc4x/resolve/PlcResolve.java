package com.yunyoucloud.plc4x.resolve;

import com.yunyoucloud.plc4x.core.PlcParseData;
import org.apache.plc4x.java.api.messages.PlcReadResponse;

public interface PlcResolve {
	
	/**
	 * 解析数据
	 *
	 * @param plcParseData    解析数据
	 * @param tagName         标签名称
	 * @param plcReadResponse plc读取响应
	 * @return 解析结果
	 */
	Object resolve(PlcParseData plcParseData, String tagName, PlcReadResponse plcReadResponse);
	
	/**
	 * 提取数据
	 *
	 * @param targetFiledValueDb 目标DB实体字段的值
	 * @param plcParseData       解析数据
	 * @return 提取结果
	 */
	Object extract(Object targetFiledValueDb, PlcParseData plcParseData);
}
