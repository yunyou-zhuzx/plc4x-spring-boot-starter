package com.yunyoucloud.plc4x.service.opcua;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import lombok.Data;

@Data
public class Material22 {
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|AGV正在进入",
		display = "AGV正在进入"
	)
	private Boolean agvInProgress;
	
	public final static String AGV_IND_FIELD_NAME = "agvInProgress";
	
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
	
	public final static String DOWNLOAD_TRAY_FINISH_FIELD_NAME = "finishDownloadTray";
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|mes发送启动",
		display = "mes发送启动"
	)
	private Boolean start;
	
	public final static String START_FIELD_NAME = "start";
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|mes发送暂停",
		display = "mes发送暂停"
	)
	private Boolean pause;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|mes发送当前任务号",
		display = "mes发送当前任务号"
	)
	private Short taskId;
	
	@PlcVariable(
		type = EDataType.INT16,
		address = "ns=4;s=HMI|mes发送当前托盘号",
		display = "mes发送当前托盘号"
	)
	private Short trayCode;
	
	@PlcVariable(
		type = EDataType.BOOL,
		address = "ns=4;s=HMI|完成数量清零",
		display = "完成数量清零"
	)
	private Boolean resetCount;
}
