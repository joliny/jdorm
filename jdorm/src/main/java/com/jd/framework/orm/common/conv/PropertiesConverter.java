package com.jd.framework.orm.common.conv;



import java.util.Properties;
import com.jd.framework.orm.common.ClassConvertUtils;

/**
 * MapConverter 的 Properties 实现
 */
public class PropertiesConverter extends MapConverter {
    protected Properties props;

    public PropertiesConverter(Properties props) {
        this.props = props;
    }

    @Override
    protected Object value(String key) {
        return props.get(key);
    }

    @Override
    protected String[] values(String key) {
        return ClassConvertUtils.convertArrays(value(key));
    }

    @Override
    public boolean exist(String key) {
        return props.containsKey(key);
    }
}
