package com.yunyoucloud.plc4x.config.connection;

import com.yunyoucloud.plc4x.core.enums.PlcProtocol;
import lombok.Data;

@Data
public class OpcuaConnectionConfig implements ConnectionConfig {
	
	private Boolean enable = false;
	
	private String ip = "192.168.1.10";
	
	private Integer port = 4840;
	
	@Override
	public boolean enable() {
		return this.enable;
	}
	
	@Override
	public String address() {
		return "opcua:tcp://" + ip +":" + port;
	}
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.OPCUA;
	}
}
