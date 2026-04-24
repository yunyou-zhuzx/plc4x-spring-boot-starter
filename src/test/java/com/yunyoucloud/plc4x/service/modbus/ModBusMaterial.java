//package com.yunyoucloud.plc4x.service.modbus;
//
//import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
//import com.yunyoucloud.plc4x.core.enums.BitMode;
//import com.yunyoucloud.plc4x.core.enums.EDataType;
//import com.yunyoucloud.plc4x.core.enums.PlcDisplayType;
//import lombok.Data;
//
//@Data
//public class ModBusMaterial {
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "0",
//		biteType = EDataType.BOOL,
//		display = "手动操作模式",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean inlineMode;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "1",
//		biteType = EDataType.BOOL,
//		display = "自动操作模式",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean autoMode;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "2",
//		biteType = EDataType.BOOL,
//		display = "远程操作模式",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean onlineMode;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "3",
//		biteType = EDataType.BOOL,
//		display = "使能状态",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean enableStatus;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "4",
//		biteType = EDataType.BOOL,
//		display = "系统运行状态",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean deviceRunStatus;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "5",
//		biteType = EDataType.BOOL,
//		display = "系统运行状态",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean alarmStatus;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "6",
//		biteType = EDataType.BOOL,
//		display = "系统运行状态",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean progressStatus;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "7",
//		biteType = EDataType.BOOL,
//		display = "机器人运动状态",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean deviceWorkStatus;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00004",
//		isBit = true,
//		bit = "10",
//		biteType = EDataType.BOOL,
//		display = "系统报警状态",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean deviceAlarmStatus;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00005:UINT[10]",
//		isBit = true,
//		biteType = EDataType.STRING,
//		display = "当前加载的工程名",
//		displayType = PlcDisplayType.STR,
//		bitMode = BitMode.HEIGHT
//	)
//	private String curProcessFile;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00054:UINT[5]",
//		isBit = true,
//		biteType = EDataType.STRING,
//		display = "当前加载的工程名",
//		displayType = PlcDisplayType.STR,
//		bitMode = BitMode.HEIGHT
//	)
//	private String writeProcessFile;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00016",
//		isBit = true,
//		bit = "4",
//		biteType = EDataType.BOOL,
//		display = "任务完成",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean taskFinish;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00065",
//		isBit = true,
//		bit = "4",
//		biteType = EDataType.BOOL,
//		display = "上料托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean uploadTrayFinish;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00065",
//		isBit = true,
//		bit = "5",
//		biteType = EDataType.BOOL,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean downloadTrayFinish;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00081:UINT[2]",
//		isBit = true,
//		biteType = EDataType.FLOAT32,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Float taskId;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00033:UINT[2]",
//		isBit = true,
//		biteType = EDataType.FLOAT32,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Float taskId2;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00077:UINT[2]",
//		isBit = true,
//		biteType = EDataType.FLOAT32,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Float form;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00100",
//		isBit = true,
//		biteType = EDataType.INT16,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Short lock;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00052",
//		isBit = true,
//		bit = "2",
//		biteType = EDataType.BOOL,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean start;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00052",
//		isBit = true,
//		bit = "7",
//		biteType = EDataType.BOOL,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean load;
//
//	@PlcVariable(
//		type = EDataType.INT16,
//		address = "4x00052",
//		isBit = true,
//		bit = "3",
//		biteType = EDataType.BOOL,
//		display = "成品托盘到位",
//		displayType = PlcDisplayType.STR
//	)
//	private Boolean pause;
//}
