package com.yunyoucloud.plc4x.service.modbus;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.BitMode;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.core.enums.PlcDisplayType;
import lombok.Data;

@Data
public class ModBusMaterial2 {
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.0",
		display = "手动操作模式",
		displayType = PlcDisplayType.STR
	)
	private Boolean inlineMode;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.1",
		display = "自动操作模式",
		displayType = PlcDisplayType.STR
	)
	private Boolean autoMode;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.2",
		display = "远程操作模式",
		displayType = PlcDisplayType.STR
	)
	private Boolean onlineMode;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.3",
		display = "使能状态",
		displayType = PlcDisplayType.STR
	)
	private Boolean enableStatus;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.4",
		display = "系统运行状态",
		displayType = PlcDisplayType.STR
	)
	private Boolean deviceRunStatus;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.5",
		display = "系统运行状态",
		displayType = PlcDisplayType.STR
	)
	private Boolean alarmStatus;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.6",
		display = "系统运行状态",
		displayType = PlcDisplayType.STR
	)
	private Boolean progressStatus;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.7",
		display = "机器人运动状态",
		displayType = PlcDisplayType.STR
	)
	private Boolean deviceWorkStatus;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00019.7",
		display = "加载程序执行成功",
		displayType = PlcDisplayType.STR
	)
	private Boolean loadProcessSuccess;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00019.13",
		display = "命令执行错误",
		displayType = PlcDisplayType.STR
	)
	private Boolean commandExecuteError;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00019.10",
		display = "命令执行错误",
		displayType = PlcDisplayType.STR
	)
	private Boolean waitControl;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00019.11",
		display = "命令执行错误",
		displayType = PlcDisplayType.STR
	)
	private Boolean waitCommand;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00004.10",
		display = "系统报警状态",
		displayType = PlcDisplayType.STR
	)
	private Boolean deviceAlarmStatus;
	
	@PlcVariable(
		type = EDataType.STRING,
		address = "4x00005:UINT[10]",
		display = "当前加载的工程名",
		displayType = PlcDisplayType.STR,
		bitMode = BitMode.HEIGHT
	)
	private String curProcessFile;
	
	@PlcVariable(
		type = EDataType.STRING,
		address = "4x00054:UINT[5]",
		display = "当前加载的工程名",
		displayType = PlcDisplayType.STR,
		bitMode = BitMode.HEIGHT
	)
	private String writeProcessFile;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00016.4",
		display = "任务完成",
		displayType = PlcDisplayType.STR
	)
	private Boolean taskFinish;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00065.4",
		display = "上料托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Boolean uploadTrayFinish;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00065.5",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Boolean downloadTrayFinish;
	
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "4x00081:UINT[2]",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Float taskId;
	
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "4x00033:UINT[2]",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Float taskId2;
	
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "4x00077:UINT[2]",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Float form;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "4x00100",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Short lock;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00052.2",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Boolean start;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00052.7",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Boolean load;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "4x00052.3",
		display = "成品托盘到位",
		displayType = PlcDisplayType.STR
	)
	private Boolean pause;
}
