package com.jd.framework.orm.dialect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class SqlType {
    protected String name;
    protected Integer length;
    protected Integer scale;

    /**
     * @param defination   examples: text, char(20), decimal(10,2)
     * @return
     */
    public static SqlType newInstance(String defination) {
        SqlType info = new SqlType();
        info.name = StringUtils.substringBefore(defination, "(");
        if (defination.indexOf(")") > 0) {
            if (defination.indexOf(",") > 0) {
                info.length = Integer.valueOf(StringUtils.substringBetween(defination, "(", ",").trim());
                info.scale = Integer.valueOf(StringUtils.substringBetween(defination, ",", ")").trim());
            } else {
                info.length = Integer.valueOf(StringUtils.substringBetween(defination, "(", ")").trim());
            }
        }
        return info;
    }

    public static SqlType newInstance(String name, Integer length, Integer scale) {
        SqlType info = new SqlType();
        info.name = name;
        info.length = length;
        info.scale = scale;
        return info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;

        SqlType rhs = (SqlType) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(obj));
        builder.append(name, rhs.name);
        builder.append(length, rhs.length);
        builder.append(scale, rhs.scale);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        if (length != null) {
            if (scale == null) {
                return name + "(" + length.toString() + ")";
            } else {
                return name + "(" + length.toString() + "," + scale.toString() + ")";
            }
        } else {
            return name;
        }
    }
}
