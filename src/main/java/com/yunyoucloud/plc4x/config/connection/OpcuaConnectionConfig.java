package com.yunyoucloud.plc4x.config.connection;

import com.yunyoucloud.plc4x.common.enums.PlcProtocol;
import lombok.Data;

@Data
public class OpcuaConnectionConfig implements ConnectionConfig {
	
	private Boolean enable = false;
	
	private String ip = "192.168.1.10";
	
	private String dd = "";
	
	@Override
	public boolean enable() {
		return this.enable;
	}
	
	@Override
	public String address() {
		return "opcua:tcp://" + ip;
	}
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.OPCUA;
	}
}
