package com.jd.framework.orm.schema;

/**
 * 只读的数据库字段，由SchemaInfoImpl初始化
 */
public class SchemaColumn {
    protected String fieldName;
    protected Class<?> fieldClass;
    protected String columnName;
    protected String typeName;
    protected Integer typeLength;
    protected Integer typeScale;
    protected Object  fieldValue;
    
    protected boolean nullable;
    protected boolean primaryKey;
    protected boolean istransient;

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getTypeLength() {
        return typeLength;
    }

    public Integer getTypeScale() {
        return typeScale;
    }

    public boolean isNullable() {
        return nullable;
    }


    public boolean isPrimaryKey() {
        return primaryKey;
    }

	/**
	 * @return the fieldValue
	 */
	public Object getFieldValue() {
		return fieldValue;
	}

	/**
	 * @param fieldValue the fieldValue to set
	 */
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @param fieldClass the fieldClass to set
	 */
	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @param typeLength the typeLength to set
	 */
	public void setTypeLength(Integer typeLength) {
		this.typeLength = typeLength;
	}

	/**
	 * @param typeScale the typeScale to set
	 */
	public void setTypeScale(Integer typeScale) {
		this.typeScale = typeScale;
	}

	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean getIstransient() {
		return istransient;
	}

	public void setIstransient(boolean istransient) {
		this.istransient = istransient;
	}

	
    
}
