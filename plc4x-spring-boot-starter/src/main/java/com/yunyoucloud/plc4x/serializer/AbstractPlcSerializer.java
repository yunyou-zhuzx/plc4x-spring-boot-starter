package com.yunyoucloud.plc4x.serializer;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.PlcParseData;
import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.core.enums.EDataType;
import com.yunyoucloud.plc4x.resolve.PlcResolve;
import com.yunyoucloud.plc4x.utils.PLCUtils;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPlcSerializer implements IPLCSerializable {
	
	protected final PLC plc;
	
	public AbstractPlcSerializer(final PLC plc) {
		this.plc = plc;
	}
	
	/**
	 * 解析不通协议的地址
	 *
	 * @param dbAddress 多模式下使用，统一的DB块，但是IP不通
	 * @param address   具体的IP地址
	 * @param dataType  地址类型
	 * @return plc4j 的可识别地址
	 */
	public abstract String resolveAddress(String dbAddress, String address, EDataType dataType);
	
	/**
	 * 解析不通协议的地址
	 *
	 * @param dbAddress   多模式下使用，统一的DB块，但是IP不通
	 * @param plcVariable 变量注解
	 * @return plc4j 的可识别地址
	 */
	public String resolveAddress(String dbAddress, PlcVariable plcVariable) {
		return resolveAddress(dbAddress, plcVariable.address(), plcVariable.type());
	}
	
	/**
	 * 解析位地址
	 *
	 * @param dbAddress 地址
	 * @return 位地址
	 */
	public String resolveBits(String dbAddress) {
		return "[16]";
	}
	
	/**
	 * 获取 plc 解析器
	 *
	 * @return plc 解析器
	 */
	public abstract PlcResolve getPlcResolve();
	
	@Override
	public <T> T read(final Class<T> db) {
		return read(db, null);
	}
	
	@Override
	public <T> T read(final Class<T> db, final Integer index) {
		final List<PlcParseData> plcParseData = parseBean(db, index);
		this.plc.read(plcParseData, getPlcResolve());
		return this.fillData(db, plcParseData);
	}
	
	@Override
	public <T> void write(T db) {
		write(db, null);
	}
	
	@Override
	public <T> void write(T db, final Integer index) {
		final List<PlcParseData> plcParseData = parseBean(db.getClass(), index);
		extractField(db, plcParseData, getPlcResolve());
		this.plc.write(
			plcParseData
				.stream()
				.filter(item -> Objects.nonNull(item.getResponseItem().getValue()))
				.toList()
		);
	}
	
	private List<PlcParseData> parseBean(final Class<?> targetClass, final Integer index) {
		List<PlcParseData> plcParseData = new ArrayList<>();
		for (Field field : targetClass.getDeclaredFields()) {
			PlcVariable plcVariable = field.getAnnotation(PlcVariable.class);
			if (Objects.nonNull(plcVariable)) {
				plcParseData.addAll(this.createPlcParseData(targetClass, plcVariable, field, index));
			}
		}
		return plcParseData;
	}
	
	private List<PlcParseData> createPlcParseData(
		final Class<?> targetClass,
		final PlcVariable plcVariable,
		final Field field,
		final Integer index
	) {
		String dbAddress = "";
		if (Objects.nonNull(index)) {
			dbAddress = PLCUtils.getDbAddressInMultiMode(targetClass, index);
		}
		final PlcParseData plcParseData = new PlcParseData();
		plcParseData.setField(field);
		plcParseData.setBitMode(plcVariable.bitMode());
		plcParseData.setBits(resolveBits(plcVariable.address()));
		plcParseData.setDataType(plcVariable.type());
		plcParseData.setCount(plcVariable.count());
		plcParseData.getRequestItem().setTagName(field.getName());
		plcParseData.getRequestItem().setAddress(resolveAddress(dbAddress, plcVariable));
		return List.of(plcParseData);
	}
	
	@SneakyThrows
	private <T> T fillData(Class<T> targetDbClass, List<PlcParseData> plcParseData) {
		T result = targetDbClass.newInstance();
		for (PlcParseData plcParseDatum : plcParseData) {
			fillField(result, plcParseDatum);
		}
		return result;
	}
	
	@SneakyThrows
	private <T> void fillField(T targetDb, PlcParseData plcParseDatum) {
		plcParseDatum.getField().setAccessible(true);
		plcParseDatum.getField().set(targetDb, plcParseDatum.getResponseItem().getValue());
	}
	
	@SneakyThrows
	private <T> void extractField(T targetDb, List<PlcParseData> plcParseDataList, final PlcResolve plcResolve) {
		for (PlcParseData plcParseDatum : plcParseDataList) {
			extractField(targetDb, plcParseDatum, plcResolve);
		}
	}
	
	@SneakyThrows
	private <T> void extractField(T targetDb, PlcParseData plcParseData, final PlcResolve plcResolve) {
		plcParseData.getField().setAccessible(true);
		final Object targetFiledValueDb = plcParseData.getField().get(targetDb);
		Object value = plcResolve.extract(targetFiledValueDb, plcParseData);
		plcParseData.getResponseItem().setValue(value);
	}
}
