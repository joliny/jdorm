/**
 * 
 */
package com.jd.framework.orm.entity;

import java.io.Serializable;

import javax.sql.DataSource;
import com.jd.framework.orm.schema.SchemaInfo;
import com.jd.framework.orm.sequenceid.JdbcSequenceIdProvider;
import com.jd.framework.orm.util.SpringContextUtil;


@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable, Cloneable{
	
    //生成并返回ID
    public  Integer generateId(String datasource,String tablename){
    	DataSource source=(DataSource) SpringContextUtil.getBean(datasource);
        JdbcSequenceIdProvider sequenceIdProvider=new JdbcSequenceIdProvider(source);
		return sequenceIdProvider.create(tablename).nextVal();
    	
    }
    /**
     * 转为字符
     * @return
     */
    public abstract String toJsonObject();
    /**
     * 生成表的信息
     * @return
     */
    public abstract SchemaInfo getSchemaInfo();
}
