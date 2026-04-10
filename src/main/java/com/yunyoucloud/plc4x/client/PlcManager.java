package com.yunyoucloud.plc4x.client;

import com.yunyoucloud.plc4x.config.connection.ConnectionConfig;
import com.yunyoucloud.plc4x.exception.PlcCommExpection;
import org.apache.plc4x.java.DefaultPlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;

import java.util.Objects;

public class PlcManager {
	
	public static PLC getPlc(final ConnectionConfig connectionConfig) {
		try {
			PlcConnection plcConnection = new DefaultPlcDriverManager().getConnection(connectionConfig.address());
			return new PLC(plcConnection, connectionConfig.protocol());
		} catch (Exception e) {
			throw new PlcCommExpection(e.getMessage());
		}
	}
	
	public static void reconnect(final PLC plc) {
		if (Objects.isNull(plc) || Objects.isNull(plc.getPlcConnection())) {
			throw new PlcCommExpection("no connection");
		}
		
		if (!plc.getPlcConnection().isConnected()) {
			try {
				plc.getPlcConnection().connect();
			} catch (Exception e) {
				throw new PlcCommExpection(e.getMessage());
			}
		}
	}
	
	public static void close(final PLC plc) {
		if (Objects.isNull(plc) || Objects.isNull(plc.getPlcConnection())) {
			throw new PlcCommExpection("no connection");
		}
		
		if (plc.getPlcConnection().isConnected()) {
			try {
				plc.getPlcConnection().close();
			} catch (Exception e) {
				throw new PlcCommExpection(e.getMessage());
			}
		}
	}
}
