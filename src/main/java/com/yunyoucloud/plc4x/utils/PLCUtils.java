package com.yunyoucloud.plc4x.utils;

import com.yunyoucloud.plc4x.client.PLC;
import com.yunyoucloud.plc4x.core.annotations.DbMultiMode;
import com.yunyoucloud.plc4x.core.annotations.PlcVariable;
import com.yunyoucloud.plc4x.exception.PlcCommExpection;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class PLCUtils {

    public List<String> getAllDbAddressInMultiMode(final Class<?> targetClass) {
        final DbMultiMode multiMode = targetClass.getAnnotation(DbMultiMode.class);
        if (Objects.isNull(multiMode)) {
            // 缺少MultiMode
            throw new PlcCommExpection("lack annotation DbMultiMode in multi mode");
        }

        return Arrays.stream(multiMode.values()).toList();
    }

    public String getDbAddressInMultiMode(final Class<?> targetClass, final Integer index) {
        final DbMultiMode multiMode = targetClass.getAnnotation(DbMultiMode.class);
        if (Objects.isNull(multiMode)) {
            // 缺少DbMultiMode
            throw new PlcCommExpection("lack annotation DbMultiMode in multi mode");
        }

        if (Objects.isNull(index)) {
            // 缺少DbMultiMode
            throw new PlcCommExpection("index is null in multi mode");
        }

        if (multiMode.values().length < index || index <= 0) {
            throw new PlcCommExpection("index out of length in multi mode");
        }

        return multiMode.values()[index - 1];
    }

    @SneakyThrows
    public String getAddress(final Class<?> dbClass, final String fieldName) {
        return getAddress(dbClass.getDeclaredField(fieldName));
    }

    @SneakyThrows
    public String getAddress(final Class<?> dbClass, final String fieldName, final Integer index) {
        return getAddress(dbClass.getDeclaredField(fieldName), index);
    }

    @SneakyThrows
    public String getAddress(final Field field) {
        field.setAccessible(true);
        final PlcVariable plcVariable = field.getAnnotation(PlcVariable.class);
        return Optional.ofNullable(plcVariable).map(PlcVariable::address).orElse("");
    }

    @SneakyThrows
    public String getAddress(final Field field, final Integer index) {
        final String dbAddressInMultiMode = getDbAddressInMultiMode(field.getDeclaringClass(), index);
        field.setAccessible(true);

        final PlcVariable plcVariable = field.getAnnotation(PlcVariable.class);
        return Optional.ofNullable(plcVariable)
                .map(PlcVariable::address)
                .map(item -> dbAddressInMultiMode + "." + item)
                .orElse("");
    }

    @SneakyThrows
    public <T> boolean write(final PLC plc, final T dbData) {
        return write(plc, dbData, null, false);
    }

    @SneakyThrows
    public <T> boolean write(final PLC plc, final T dbData, final Integer index) {
        return write(plc, dbData, index, true);
    }

    @SneakyThrows
    private <T> boolean write(final PLC plc, final T dbData, final Integer index, final boolean isMultiMode) {
        final Field[] fields = dbData.getClass().getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            final PlcVariable plcVariable = field.getAnnotation(PlcVariable.class);
            final String address = isMultiMode ? getAddress(field, index) : getAddress(field);
            // 有注解信息，并且值不为空
            final Object objValue = field.get(dbData);
            if (Objects.nonNull(plcVariable) && !address.isBlank() && Objects.nonNull(objValue)) {
                switch (plcVariable.type()) {
                    case STRING -> plc.writeString(address, objValue.toString());
                    case INT16 -> plc.writeShort(address, (Short) objValue);
                    case INT32, UINT16 -> plc.writeInteger(address, (Integer) objValue);
                    case INT64, UINT32 -> plc.writeLong(address, (Long) objValue);
					case BOOL -> plc.writeBoolean(address, (Boolean) objValue);
                    case FLOAT32 -> plc.writeFloat(address, (Float) objValue);
                    case FLOAT64 -> plc.writeDouble(address, (Double) objValue);
                    case BYTE -> plc.writeByte(address, (byte) objValue);
                    default -> {
                    }
                }
            }
        }
        return true;
    }

}
