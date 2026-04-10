package com.yunyoucloud.plc4x.config.connection;

import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import lombok.Data;

import java.util.Objects;

@Data
public class ModbusConnectionConfig implements ConnectionConfig {
	
	private Boolean enable = false;
	
	private String ip = "192.168.1.10";
	
	private Integer port = 502;
	
	private Integer slave = 1;
	
	@Override
	public boolean enable() {
		return this.enable;
	}
	
	@Override
	public String address() {
		if (Objects.equals(this.port, 502)) {
			return "modbus-tcp://" + ip + "?default-unit-identifier=" + this.slave;
		}
		return "modbus-tcp://" + ip + ":" + port + "?default-unit-identifier=" + this.slave;
	}
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.MODBUS_TCP;
	}
}
