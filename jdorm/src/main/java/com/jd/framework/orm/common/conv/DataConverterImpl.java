package com.jd.framework.orm.common.conv;

import com.jd.framework.orm.common.ClassConvertUtils;


/**
 * DataConverter 的默认实现
 */
public class DataConverterImpl extends DataConverter {

    protected Object value;

    public DataConverterImpl(Object value) {
        this.value = value;
    }

    @Override
    protected Object value() {
        return value;
    }

    @Override
    protected String[] values() {
        return ClassConvertUtils.convertArrays(value());
    }

    @Override
    public boolean exist() {
        return value() != null;
    }
}
