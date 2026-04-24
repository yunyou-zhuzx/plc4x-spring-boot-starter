package com.yunyoucloud.plc4x.service.opcua;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import lombok.Data;

@Data
public class Material {
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|发送mes手动中",
		display = "发送mes手动中"
	)
	private Boolean inlineMode;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|产品1完成数量",
		display = "产品1完成数量"
	)
	private Short prodProcessCount1;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|产品2完成数量",
		display = "产品2完成数量"
	)
	private Short prodProcessCount2;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|产品3完成数量",
		display = "产品3完成数量"
	)
	private Short prodProcessCount3;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|产品4完成数量",
		display = "产品4完成数量"
	)
	private Short prodProcessCount4;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|产品5完成数量",
		display = "产品5完成数量"
	)
	private Short prodProcessCount5;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|发送mes任务完成",
		display = "发送mes任务完成"
	)
	private Boolean taskFinish;
	
	public final static String TASK_FINISH_FIELD_NAME = "taskFinish";
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|发送mes当前任务号",
		display = "发送mes当前任务号"
	)
	private Short taskId;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|发送mes当前托盘号",
		display = "发送mes当前托盘号"
	)
	private Short trayCode;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|发送mes当前作业中",
		display = "发送mes当前作业中"
	)
	private Boolean deviceProgress;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|发送mes当前故障",
		display = "发送mes当前故障"
	)
	private Boolean deviceException;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|发送mes当前空闲",
		display = "发送mes当前空闲"
	)
	private Boolean deviceReady;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|申请mes换成品托盘",
		display = "申请mes换成品托盘"
	)
	private Boolean applySwitchDownloadTray;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|AGV正在进入",
		display = "AGV正在进入"
	)
	private Boolean agvInProgress;
	
	public final static String AGV_IN_FINISH_FIELD_NAME = "agvInProgress";
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|mes上料配盘到位",
		display = "mes上料配盘到位"
	)
	private Boolean finishUploadTray;
	
	public final static String UPLOAD_TRAY_FINISH_FIELD_NAME = "finishUploadTray";
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|mes成品托盘到位",
		display = "mes成品托盘到位"
	)
	private Boolean finishDownloadTray;
}
