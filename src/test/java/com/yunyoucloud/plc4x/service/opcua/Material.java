package com.yunyoucloud.plc4x.service.opcua;

import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import lombok.Data;

@Data
public class Material {
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|AGV正在进入")
	private Boolean agvZaiJinRu;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|mes上料无料")
	private Boolean mesShangLiaoWuLiao;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|mes上料配盘到位")
	private Boolean mesShangLiaoPeiPanDingWei;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|mes发送启动")
	private Boolean mesFaSongQiDong;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|mes发送暂停")
	private Boolean mesFaSongZanTing;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|mes成品托盘到位")
	private Boolean mesChengPinPanDuoDingWei;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|产品1完成数量")
	private Short chanPin1WanChengShuLiang;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|产品2完成数量")
	private Short chanPin2WanChengShuLiang;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|产品3完成数量")
	private Short chanPin3WanChengShuLiang;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|产品4完成数量")
	private Short chanPin4WanChengShuLiang;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|产品5完成数量")
	private Short chanPin5WanChengShuLiang;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|发送mes任务完成")
	private Boolean faSongMesRenWuWanCheng;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|发送mes当前任务号")
	private Short faSongMesDangQianRenWuHao;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|发送mes当前作业中")
	private Boolean faSongMesDangQianZuoYeZhong;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|发送mes当前故障")
	private Boolean faSongMesDangQianGuZhang;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|发送mes当前空闲")
	private Boolean faSongMesDangQianKongXian;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|完成数量清零")
	private Boolean wanChengShuLiangQingLing;
	
	@PlcVariable(type = EDataType.BOOL, address = "ns=4;s=HMI|申请mes换成成品托盘")
	private Boolean shenQingMesHuanChengChengPinPanDuo;
	
	@PlcVariable(type = EDataType.INT16, address = "ns=4;s=HMI|远程配方")
	private Short yuanChengPeiFang;
}
