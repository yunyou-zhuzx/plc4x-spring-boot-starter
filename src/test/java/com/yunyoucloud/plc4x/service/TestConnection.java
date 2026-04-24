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
import com.yunyoucloud.plc4x.service.modbus.ModBusMaterial;
import com.yunyoucloud.plc4x.service.opcua.Material;
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
				final IPLCSerializable iplcSerializable = IPLCSerializable.newInstance(plc);
				final short i = iplcSerializable.readShort("DB2001.0");
				System.out.println(i);
//				final WarehouseReceiveDB read = iplcSerializable.read(WarehouseReceiveDB.class);
//				System.out.println(read);
			});
	}
	
	@Test
	public void testConnectionModbusTcp() {
		plcConfig.getPlcConfigs()
			.stream().filter(item -> item instanceof ModbusPLcConfig)
			.forEach(modbusPLcConfig -> {
				final PLC plc = PlcManager.getPlc(modbusPLcConfig.getConnections().get("plc1"));
				final IPLCSerializable iplcSerializable = IPLCSerializable.newInstance(plc);
				final ModBusMaterial read = iplcSerializable.read(ModBusMaterial.class);
				System.out.println(read);
				final ModBusMaterial modBusMaterial = new ModBusMaterial();
				modBusMaterial.setLock((short)17);
				modBusMaterial.setDownloadTrayFinish(true);
				iplcSerializable.write(modBusMaterial);
				modBusMaterial.setUploadTrayFinish(true);
				modBusMaterial.setTaskId(2.0f);
				modBusMaterial.setForm(1.0f);
				modBusMaterial.setWriteProcessFile("seut.namni");
				modBusMaterial.setLoad(true);
				iplcSerializable.write(modBusMaterial);
				final ModBusMaterial modBusMaterial2 = new ModBusMaterial();
				modBusMaterial2.setStart(true);
				iplcSerializable.write(modBusMaterial2);
				final ModBusMaterial read2 = iplcSerializable.read(ModBusMaterial.class);
				System.out.println(read2);
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
				Material read = iplcSerializable.read(Material.class);
				System.out.println(read);
			});
	}
	
}
