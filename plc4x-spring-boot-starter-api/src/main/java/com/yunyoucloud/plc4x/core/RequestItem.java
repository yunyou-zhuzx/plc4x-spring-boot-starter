package com.yunyoucloud.plc4x.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class RequestItem {
	
	private String tagName;
	private String address;
}
