package com.yunyoucloud.plc4x.config.connection;

import com.yunyoucloud.plc4x.common.enums.PlcProtocol;
import lombok.Data;

@Data
public class S7ConnectionConfig implements ConnectionConfig {
	
	private Boolean enable = false;
	
	private String ip = "192.168.1.10";
	
	private Integer port = 102;
	
	private Integer rack = 0;
	
	private Integer slot = 1;
	
	private Integer pduLength = 240;
	
	@Override
	public boolean enable() {
		return this.enable;
	}
	
	@Override
	public String address() {
		return "s7://" + ip;
	}
	
	@Override
	public PlcProtocol protocol() {
		return PlcProtocol.S7;
	}
}
