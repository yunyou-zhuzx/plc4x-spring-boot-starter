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
public class WarehouseSendDB{
	
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
		address = "DB2000.0",
		display = "在线状态",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "1-在线,0-离线"
	)
	private Short online;
	
	/**
	 * WCS任务启动/停止 0:停止 1:启动
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.2",
		display = "任务启动/停止",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "0-停止,1-启动"
	)
	private Short status;
	
	public final static String COMMAND_FIELD_NAME = "status";
	
	/**
	 * 任务号
	 */
	@PlcVariable(
		type = EDataType.STRING,
		count = 20,
		address = "DB2000.4[20]",
		display = "任务编号",
		displayType = PlcDisplayType.STR
	)
	private String taskCode;
	
	/**
	 * 任务类型 0-无任务，1-入库，2-出库，3-越库
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.260",
		display = "任务类型",
		displayType = PlcDisplayType.OPTION,
		displayConfig = "0-无任务,1-入库,2-出库,3-越库"
	)
	private Short taskType;
	
	/**
	 * 出库行号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.262",
		display = "出库行号",
		displayType = PlcDisplayType.STR
	)
	private Short outWarehouseRow;
	
	/**
	 * 出库列号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.264",
		display = "出库列号",
		displayType = PlcDisplayType.STR
	)
	private Short outWarehouseColumn;
	
	/**
	 * 出库层号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.266",
		display = "出库层号",
		displayType = PlcDisplayType.STR
	)
	private Short outWarehouseLayer;
	
	/**
	 * 入库行号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.268",
		display = "入库行号",
		displayType = PlcDisplayType.STR
	)
	private Short inWarehouseRow;
	
	/**
	 * 入库列号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.270",
		display = "入库列号",
		displayType = PlcDisplayType.STR
	)
	private Short inWarehouseColumn;
	
	/**
	 * 入库层号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.272",
		display = "入库层号",
		displayType = PlcDisplayType.STR
	)
	private Short inWarehouseLayer;
	
	/**
	 * 目标平台号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.276",
		display = "目标平台号",
		displayType = PlcDisplayType.STR
	)
	private Short targetPlatformCode;
	
	/**
	 * 目标平台车号
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.278",
		display = "目标平台车号",
		displayType = PlcDisplayType.STR
	)
	private Short targetPlatformCarCode;
	
	/**
	 * 目标站台车到位
	 */
	@PlcVariable(
		type = EDataType.INT16,
		address = "DB2000.278",
		display = "目标站台车到位",
		displayType = PlcDisplayType.STR
	)
	private Short targetPlatformCarRight;
	
	/**
	 * 产品序列号
	 */
	@PlcVariable(
		type = EDataType.STRING,
		count = 20,
		address = "DB2000.280[20]",
		display = "产品序列号",
		displayType = PlcDisplayType.STR
	)
	private String prodSerial;
	
	/**
	 * 箱体名称
	 */
	@PlcVariable(
		type = EDataType.STRING,
		count = 20,
		address = "DB2000.536[20]",
		display = "箱体名称",
		displayType = PlcDisplayType.STR
	)
	private String materialName;
	
	/**
	 * 箱体长度
	 */
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "DB2000.792",
		needDisplay = false
	)
	private Float materialLength;
	
	/**
	 * 箱体宽度
	 */
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "DB2000.796",
		needDisplay = false
	)
	private Float materialWidth;
	
	/**
	 * 箱体高度
	 */
	@PlcVariable(
		type = EDataType.FLOAT32,
		address = "DB2000.780",
		needDisplay = false
	)
	private Float materialHeight;
	
}
