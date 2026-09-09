package com.yunyoucloud.plc4x.service;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.client.PlcManager;
import com.yunyoucloud.plc4x.config.IPlcConfig;
import com.yunyoucloud.plc4x.config.ModbusPLcConfig;
import com.yunyoucloud.plc4x.config.OpcuaPlcConfig;
import com.yunyoucloud.plc4x.config.PlcConfig;
import com.yunyoucloud.plc4x.config.S7PlcConfig;
import com.yunyoucloud.plc4x.serializer.IPLCSerializable;
import com.yunyoucloud.plc4x.serializer.OpcuaSerializable;
import com.yunyoucloud.plc4x.service.modbus.ModBusMaterial2;
import com.yunyoucloud.plc4x.service.opcua.Material;
import com.yunyoucloud.plc4x.service.opcua.Material22;
import com.yunyoucloud.plc4x.service.s7.WarehouseSendDB;
import lombok.SneakyThrows;
import org.apache.plc4x.java.DefaultPlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestConnection {
	
	@Autowired
	private PlcConfig plcConfig;
	
	@Test
	public void test() {
		try (PlcConnection plcConnection = new DefaultPlcDriverManager().getConnection("opcua:tcp://192.168.1.88:4840")) {
			final IPLCSerializable opcuaSerializer = OpcuaSerializable.newInstance(PlcManager.getPlc(null));
			final Material read = opcuaSerializer.read(Material.class);
			System.out.println(read);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testConfig() {
		for (IPlcConfig config : plcConfig.getPlcConfigs()) {
			System.out.println(config.isEnable());
			config.getConnections().forEach((name, connection) -> {
				System.out.println(connection.enable() + " " + connection.address());
			});
		}
	}
	
	@Test
	public void testConnectionS7() {
		plcConfig.getPlcConfigs()
			.stream().filter(item -> item instanceof S7PlcConfig)
			.forEach(s7config -> {
				final PLC plc = PlcManager.getPlc(s7config.getConnections().get("plc1"));
//				plc.write("%DB2000:4:STRING(20)", "TS-202609898989876659");
//				final String read = plc.read("%DB2000:4:STRING(20)", String.class);
//				System.out.println(read);
				final IPLCSerializable iplcSerializable = IPLCSerializable.newInstance(plc);
				final WarehouseSendDB warehouseSendDB = new WarehouseSendDB();
				warehouseSendDB.setMaterialName("电箱柜");
				iplcSerializable.write(warehouseSendDB);
				System.out.println(iplcSerializable.read(WarehouseSendDB.class));



//				final short i = iplcSerializable.readShort("DB2001.0");
//				System.out.println(i);
//				final WarehouseReceiveDB read = iplcSerializable.read(WarehouseReceiveDB.class);
//				System.out.println(read);
			});
	}
	
	@SneakyThrows
	private void sendControlCommand(final IPLCSerializable iplcSerializable, final String address) {
		ModBusMaterial2 modBusMaterial2 = new ModBusMaterial2();
		modBusMaterial2.setLock((short) 0);
		iplcSerializable.write(modBusMaterial2);
		TimeUnit.SECONDS.sleep(1);
		
		boolean isFinish = false;
		final long l = System.currentTimeMillis();
		while (!isFinish) {
			final ModBusMaterial2 read22 = iplcSerializable.read(ModBusMaterial2.class);
			if (read22.getWaitControl()) {
				modBusMaterial2.setLock((short) 17);
				iplcSerializable.write(modBusMaterial2);
				TimeUnit.SECONDS.sleep(1);
			}
			if (read22.getWaitCommand()) {
				iplcSerializable.writeBoolean(address, true);
				isFinish = true;
			}
			if(isFinish) {
				TimeUnit.SECONDS.sleep(1);
				iplcSerializable.writeBoolean(address, false);
			}
			if (System.currentTimeMillis() - l > 5000) {
				break;
			}
		}
	}
	
	@Test
	public void testConnectionModbusTcp() {
		plcConfig.getPlcConfigs()
			.stream().filter(item -> item instanceof ModbusPLcConfig)
			.forEach(modbusPLcConfig -> {
				final PLC plc = PlcManager.getPlc(modbusPLcConfig.getConnections().get("plc1"));
				final IPLCSerializable iplcSerializable = IPLCSerializable.newInstance(plc);
				final ModBusMaterial2 read = iplcSerializable.read(ModBusMaterial2.class);
				System.out.println(read);
				
				iplcSerializable.writeBoolean("4x00065.4", true);
				iplcSerializable.writeBoolean("4x00065.5", true);
				
				ModBusMaterial2 modBusMaterial2 = new ModBusMaterial2();
				modBusMaterial2.setWriteProcessFile("seut.namni");
				iplcSerializable.write(modBusMaterial2);
				sendControlCommand(iplcSerializable, "4x00052.7");
				
				ModBusMaterial2 modBusMaterial3 = new ModBusMaterial2();
				modBusMaterial3.setForm(1.0f);
				iplcSerializable.write(modBusMaterial3);
				
				sendControlCommand(iplcSerializable, "4x00052.2");
				
				final ModBusMaterial2 read2 = iplcSerializable.read(ModBusMaterial2.class);
				System.out.println(read2);
				
//				final ModBusMaterial modBusMaterial = new ModBusMaterial();
//				modBusMaterial.setLock((short)17);
//				modBusMaterial.setDownloadTrayFinish(true);
//				iplcSerializable.write(modBusMaterial);
//				modBusMaterial.setUploadTrayFinish(true);
//				modBusMaterial.setTaskId(2.0f);
//				modBusMaterial.setForm(1.0f);
//				modBusMaterial.setWriteProcessFile("seut.namni");
//				modBusMaterial.setLoad(true);
//				iplcSerializable.write(modBusMaterial);
//				final ModBusMaterial modBusMaterial2 = new ModBusMaterial();
//				modBusMaterial2.setStart(true);
//				iplcSerializable.write(modBusMaterial2);
//				final ModBusMaterial read2 = iplcSerializable.read(ModBusMaterial.class);
//				System.out.println(read2);
			while (true){
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			});
	}
	
	@Test
	public void testConnectionOpcua() {
		plcConfig.getPlcConfigs()
			.stream().filter(item -> item instanceof OpcuaPlcConfig)
			.forEach(opcuaPlcConfig -> {
				final PLC plc = PlcManager.getPlc(opcuaPlcConfig.getConnections().get("plc4"));
				final IPLCSerializable iplcSerializable = IPLCSerializable.newInstance(plc);
				Material22 read = iplcSerializable.read(Material22.class);
				System.out.println(read);
			});
	}
	
}
