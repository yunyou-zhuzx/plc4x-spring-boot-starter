package com.yunyoucloud.plc4x.service.s7;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.core.enums.PlcDisplayType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class WarehouseReceiveDB {
	
	/**
	 * 心跳
	 */
	private Boolean bHeart = false;
	
	/**
	 * 是否整个对象写入
	 */
	private Boolean isAllWrite = true;
	
	/**
	 * 在线状态 1:在线 0:离线
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.0",
		display = "在线状态",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "1-在线,0-离线"
	)
	private Short online;
	
	/**
	 * 状态 0-待机；1-运行；2-故障；3-暂停
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.2",
		display = "设备状态",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "0-待机,1-运行,2-故障,3-暂停"
	)
	private Short status;
	
	/**
	 * PLC 异常代码
	 */
	@PlcVariable(
		type = EDataType.STRING,
		count = 20,
		address = "DB2001.4",
		display = "异常代码",
		displayType = PlcDisplayType.STR
	)
	private String errorCode;
	
	/**
	 * 模式 0-单机模式；1-联机模式
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.26",
		display = "模式",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "0-单机模式,1-联机模式"
	)
	private Short mode;
	
	/**
	 * 堆垛机是否有物料 0-无载；1-有载
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.28",
		display = "堆垛机是否有物料",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "0-无载,1-有载"
	)
	private Short hasMaterial;
	
	/**
	 * 堆垛机当前位置列
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.30",
		display = "堆垛机当前位置列",
		displayType = PlcDisplayType.STR
	)
	private Short positionColumn;
	
	/**
	 * 堆垛机当前位置列
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.32",
		display = "堆垛机当前位置层",
		displayType = PlcDisplayType.STR
	)
	private Short positionLayer;
	
	/**
	 * 目标平台
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.34",
		display = "目标平台",
		displayType = PlcDisplayType.STR
	)
	private Short platform;
	
	/**
	 * 任务状态 0-空闲；1-执行中；2-完成；3-异常
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.36",
		display = "任务状态",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "0-空闲,1-执行中,2-完成,3-异常"
	)
	private Short taskStatus;
	
	
	/**
	 * 任务编号
	 */
	@PlcVariable(
		type = EDataType.STRING,
		count = 20,
		address = "DB2001.38",
		display = "任务编号",
		displayType = PlcDisplayType.STR
	)
	private String taskCode;
	
	/**
	 * 堆垛机取放货完成: 0-未完成；1-完成
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2001.294",
		needDisplay = false
	)
	private Short finish;
	
	public static final String TASK_STATUS_FIELD_NAME = "finish";
	
	/**
	 * X轴坐标
	 */
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "DB2001.296",
		needDisplay = false
	)
	private Float x;
	
	/**
	 * Z轴坐标
	 */
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "DB2001.300",
		needDisplay = false
	)
	private Float z;
	
	/**
	 * Y轴坐标
	 */
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "DB2001.304",
		needDisplay = false
	)
	private Float y;
	
	public static WarehouseReceiveDB resetTaskStatus() {
		return new WarehouseReceiveDB().setIsAllWrite(false).setTaskStatus((short) 0);
	}
}
