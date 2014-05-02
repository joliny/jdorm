/*
 * 文件：test.jd.framework.orm.entity.Sequance.java
 * 日 期：Fri May 02 01:25:11 CST 2014
 */
package test.jd.framework.orm.entity;
import com.jd.framework.orm.annotation.Column;
import com.jd.framework.orm.annotation.Table;
import com.jd.framework.orm.entity.BaseEntity;
import com.jd.framework.orm.schema.SchemaColumn;
import com.jd.framework.orm.schema.SchemaInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * 
 * this file is generated by the jdorm pojo tools.
 *
 * @author <a href="mailto:liubingsmile@gmail.com">liubing</a>
 * @version 1.0.0
 */
@Table(name = "_sequance_",readschema="test",writeschema="test")
public class SequanceEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * primary key field of name
	 * 
	 */
	
	private String name;
	/**  */
	
	private String nextVal;
	
	public SequanceEntity() {
		super();
	}

	/**
	 * @return the name 
	 */
	@Column(isKey=true,name="name",isNull=false,typeLength=50 )
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nextVal 
	 */
	@Column(isKey=false,name="nextVal",isNull=false,typeLength=5592405 )
	public String getNextVal() {
		return this.nextVal;
	}
	
	/**
	 * 
	 * @param nextVal the nextVal to set
	 */
	public void setNextVal(String nextVal) {
		this.nextVal = nextVal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof SequanceEntity)) {
			return false;
		}
		SequanceEntity other = (SequanceEntity)o;
		if (null == this.name) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	

	/* (non-Javadoc)
	 * @see test.jd.framework.orm.entity.Sequance#getSchemaInfo()
	 */
	@Override
	public SchemaInfo getSchemaInfo() {
	SchemaInfo schemaInfo=new SchemaInfo();
	schemaInfo.setTableClass(SequanceEntity.class);
	schemaInfo.setReadDataBase("test");
	schemaInfo.setWriteDataBase("test");
	schemaInfo.setTableName("_sequance_");
	List<SchemaColumn> schemaColumns=new ArrayList<SchemaColumn>();
	SchemaColumn schemaColumn=new SchemaColumn();
	schemaColumn.setColumnName("name");
	schemaColumn.setFieldClass(java.lang.String.class);
	schemaColumn.setFieldName("name");
	schemaColumn.setFieldValue(name);
	schemaColumn.setIstransient(false);
	schemaColumn.setNullable(false);
	schemaColumn.setPrimaryKey(true);
	schemaColumn.setTypeName("VARCHAR");
	schemaColumn.setTypeLength(50);
	schemaColumn.setTypeScale(0);
	schemaColumns.add(schemaColumn);
	schemaColumn=new SchemaColumn();
	schemaColumn.setColumnName("nextVal");
	schemaColumn.setFieldClass(java.lang.String.class);
	schemaColumn.setFieldName("nextVal");
	schemaColumn.setFieldValue(nextVal);
	schemaColumn.setIstransient(false);
	schemaColumn.setNullable(false);
	schemaColumn.setPrimaryKey(false);
	schemaColumn.setTypeName("VARCHAR");
	schemaColumn.setTypeLength(5592405);
	schemaColumn.setTypeScale(0);
	schemaColumns.add(schemaColumn);
	schemaInfo.setColumns(schemaColumns);
	return schemaInfo;
	}

	/* (non-Javadoc)
	 * @see test.jd.framework.orm.entity.Sequance#toJsonObject()
	 */
	@Override
	public String toJsonObject() {
	return "{'name':'"+name+"','nextVal':'"+nextVal+"'}";
	}
}