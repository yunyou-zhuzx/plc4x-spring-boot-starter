package com.yunyoucloud.plc4x.service;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.client.PlcManager;
import com.yunyoucloud.plc4x.config.IPlcConfig;
import com.yunyoucloud.plc4x.config.ModbusPLcConfig;
import com.yunyoucloud.plc4x.config.PlcConfig;
import com.yunyoucloud.plc4x.config.S7PlcConfig;
import com.yunyoucloud.plc4x.serializer.IPLCSerializable;
import com.yunyoucloud.plc4x.serializer.OpcuaSerializable;
import com.yunyoucloud.plc4x.service.modbus.ModBusMaterial;
import com.yunyoucloud.plc4x.service.opcua.Material;
import com.yunyoucloud.plc4x.service.s7.WarehouseReceiveDB;
import org.apache.plc4x.java.DefaultPlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
				final WarehouseReceiveDB read = iplcSerializable.read(WarehouseReceiveDB.class);
				System.out.println(read);
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
			});
	}
	
}
