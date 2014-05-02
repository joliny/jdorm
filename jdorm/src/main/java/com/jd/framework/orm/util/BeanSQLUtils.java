/**
 * 
 */
package com.jd.framework.orm.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.schema.SchemaColumn;
import com.jd.framework.orm.schema.SchemaInfo;

public class BeanSQLUtils {
	public static final String EDIT_TYPE = "edit";//修改，保存，增加 ，删除
	public static final String QUERY_TYPE = "query";//查询  或者作为参数
	
	public static final String SORT_DIRECTION_ASC = "ASC";

	public static final String SORT_DIRECTION_DESC = "DESC";
	
	private static  Logger logger = Logger.getLogger(BeanSQLUtils.class);
	/**
	 * 创建表语句
	 * @param schema
	 * @param dialect
	 * @return
	 */
	public static String get_sql_table_create(SchemaInfo schema, Dialect dialect) {
	        StringBuilder sqls = new StringBuilder();
	        List<String> pks = new ArrayList<String>(3);

	        sqls.append("create table " + dialect.getIdentifier(schema.getTableName()) + " (\n");
	        for (SchemaColumn c : schema.getColumns()) {
	            if (c.isPrimaryKey()) {
	                pks.add(dialect.getIdentifier(c.getColumnName()));
	            }
	            sqls.append("    ");
	            sqls.append(dialect.getIdentifier(c.getColumnName()));
	            sqls.append(" ");
	            sqls.append(dialect.convertJavaStringToSqlType(c.getTypeName(), c.getTypeLength(), c.getTypeScale()));
	            sqls.append(c.isNullable() ? "" : " not null");
	            sqls.append(",\n");
	        }

	        sqls.append("    primary key (" + StringUtils.join(pks, ",") + ")\n");
	        sqls.append(");\n");

	        return sqls.toString();
	    }
	
	public static boolean validateTransient(List<SchemaColumn> schemaColumns,String column){
		Boolean flag=false;
		for(SchemaColumn schemaColumn: schemaColumns){
			if(!StringUtil.isNullOrEmpty(column)&&schemaColumn.getFieldName().equals(column)){//等价
				flag=true;
				break;
			}
		}
		
		return flag;
	}
	/**
	 * 判断是否是转义字段
	 * @param schema
	 * @return
	 */
	public static String getOrderField(List<SchemaColumn> schemaColumns,String column) {
		String orderfield="";
		for(SchemaColumn schemaColumn: schemaColumns){
			if(schemaColumn.getFieldName().equals(column)){//等价
				if(schemaColumn.getIstransient()){
					orderfield=schemaColumn.getColumnName().substring(0, schemaColumn.getColumnName().indexOf("name"));
				}else{
					orderfield=schemaColumn.getColumnName();
				}
				break;
			}
		}
		
		return orderfield;
	}
	/**
	 * 生成插入语句
	 * @param schema
	 * @param dialect
	 * @return
	 */
	public static String get_sql_insert(SchemaInfo schema,Dialect dialect) {
		  List<String> names = new ArrayList<String>();
		  List<Object> values = new ArrayList<Object>();
		  for (SchemaColumn c : schema.getColumns()) {
			  if(!StringUtil.isNullOrEmpty(c.getFieldValue())){
				  names.add(dialect.getIdentifier(c.getColumnName()));
		            values.add(c.getFieldValue());
			  } 
	        }
	        String sql = "insert into %s (%s) values (%s)";
	        String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
	        //@formatter:off
	        return String.format(sql, 
	            tableNameIdentifier,
	            StringUtils.join(names, ","), 
	            StringUtils.join(values, ",")
	        );
	}
	/**
	 * 生成插入 sql ，没有值
	 * @param schema
	 * @param dialect
	 * @return
	 */
	public static String get_sql_insert_no_value(SchemaInfo schema,Dialect dialect) {
		List<String> names = new ArrayList<String>();
		  List<Object> values = new ArrayList<Object>();
		  for (SchemaColumn c : schema.getColumns()) {
	            names.add(dialect.getIdentifier(c.getColumnName()));
	            values.add(c.getFieldValue());
	        }
	        String sql = "insert into %s (%s) values (%s)";
	        String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
	        //@formatter:off
	        return String.format(sql, 
	            tableNameIdentifier,
	            StringUtils.join(names, ","), 
	            StringUtils.repeat(":", ",", names.size())
	        );
	}
	/**
	 * 生产修改语句
	 * @param schema
	 * @param dialect
	 * @return
	 * @throws Exception
	 */
	 public static String get_sql_update(SchemaInfo schema, Dialect dialect) throws Exception{
		 if(validateKey(schema)){
			  List<String> names = new ArrayList<String>();
		        for (SchemaColumn c : schema.getColumns()) {
		            if (!c.isPrimaryKey()) {
		                names.add(dialect.getIdentifier(c.getColumnName()) + "="+c.getFieldValue()+"");
		            }
		        }
		        String sql = "update %s set %s where "+getIdBySchemaInfo(schema)+"";
		        String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
		        return String.format(sql, tableNameIdentifier, StringUtils.join(names, ","));
		 }else{
			 logger.error("表名"+schema.getTableClass().getSimpleName()+"缺少主键或者具有两个主键");
			 throw new Exception("表名"+schema.getTableClass().getSimpleName()+"缺少主键或者具有两个主键");
		 }
	 }
	 /**
	  * 生成SQL 语句
	  * @param schema
	  * @param dialect
	  * @return
	  * @throws Exception
	  */
	 public static String get_sql_update_no_value(SchemaInfo schema, Dialect dialect) throws Exception{
		 if(validateKey(schema)){
			 List<String> names = new ArrayList<String>();
		        for (SchemaColumn c : schema.getColumns()) {
		            if (!c.isPrimaryKey()) {
		                names.add(dialect.getIdentifier(c.getColumnName()) + "=?");
		            }
		        }
		        String sql = "update %s set %s where "+getKeyNameBySchemaInfo(schema);
		        String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
		        return String.format(sql, tableNameIdentifier, StringUtils.join(names, ","));
		 }else{
			 logger.error("表名"+schema.getTableClass().getSimpleName()+"缺少主键或者具有两个主键");
			 throw new Exception("表名"+schema.getTableClass().getSimpleName()+"缺少主键或者具有两个主键");
		 }
	 }
	 /**
	  * 获取删除语句
	  * @param schema
	  * @param dialect
	  * @return
	  */
	 public static String get_sql_delete(SchemaInfo schema, Dialect dialect) {
		 if(validateisKey(schema)){//具有主键
			  String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
		      return "delete from " + tableNameIdentifier + " where "+getWhereSQLBySchemaInfo(schema);
		 }else{
			 
			  List<Object> values = new ArrayList<Object>();
			  for (SchemaColumn c : schema.getColumns()) {
				  values.add(dialect.getIdentifier(c.getColumnName()) + "='"+c.getFieldValue()+"'");
		        }
		        String sql = "delete from %s  where (%s)";
		        String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
		        //@formatter:off
		        return String.format(sql, 
		            tableNameIdentifier,
		            StringUtils.join(values, ","));

		 }
	  }
	 
	 /**
	  * 根据主键生成查询语句
	  * @param schema
	  * @param dialect
	  * @return
	  */
	 public static String get_sql_select_object(SchemaInfo schema, Dialect dialect) {
	        String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
	        return "select * from " + tableNameIdentifier + " where "+getWhereSQLBySchemaInfo_novalue(schema);
	  }
	 /**
	  * 生成查询所有语句
	  * @param schema
	  * @param dialect
	  * @return
	  */
	 public static String get_sql_select_all_object(SchemaInfo schema, Dialect dialect) {
		 String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
	        return "select * from " + tableNameIdentifier;
	 }
	 /**
	  * 检查表是否有主键
	  * @param schema
	  * @return
	  */
	 public static Boolean validateisKey(SchemaInfo schema){
			Boolean flag=false;
			for (SchemaColumn c : schema.getColumns()) {
				if(c.isPrimaryKey()){//是主键
					flag=true;
					break;
				}
			}
			return flag;
		}
	 /**
	  * 根据实体类以及参数 Map 获取符合参数条件的动态SQL
	  * @param schema
	  * @param dialect
	  * @param params
	  * @return
	  */
	 public static String get_complex_sql_select_object(SchemaInfo schema, Dialect dialect,Map<String, Object> params,String orderFiled,String orderDirection){
		 String tableNameIdentifier = dialect.getIdentifier(schema.getTableName());
		 String queryEntry="select * ";
		 String[] whereBodies=getWhereInfoBySchemaInfo(schema);
		 String fromJoinSubClause = "from "+tableNameIdentifier+" ";
		 String sql = SQLUtils.generateHql(queryEntry, fromJoinSubClause,
					whereBodies, orderFiled, orderDirection, params);
		 logger.info(sql);
		 return sql;
	 }
	/**
	 * 验证表的字段是否具有主键  用于修改
	 * @param schemaInfo
	 * @return
	 */
	private static Boolean validateKey(SchemaInfo schema){
		Boolean flag=false;
		int i=0;
		for (SchemaColumn c : schema.getColumns()) {
			if(c.isPrimaryKey()){//是主键
				i++;
			}
		}
		if(i==0)
		flag=false;
		flag=true;
		return flag;
	}
	/**
	 * 获取表中主键的值 用于修改
	 * @param schema
	 * @return
	 */
	private static Object getIdBySchemaInfo(SchemaInfo schema){
		StringBuffer sb =new StringBuffer();
		for (SchemaColumn c : schema.getColumns()) {
			if(c.isPrimaryKey()){
				if(sb.length()<1){//没有增加条件
					sb.append(" "+c.getColumnName()+"="+c.getFieldValue());
				}else{
					sb.append(" and "+c.getColumnName()+"="+c.getFieldValue());
				}
			}
		}
		return sb.toString();
	}
	
	private static String getKeyNameBySchemaInfo(SchemaInfo schema){
		StringBuffer sb =new StringBuffer();
		for (SchemaColumn c : schema.getColumns()) {
			if(c.isPrimaryKey()){
				if(sb.length()<1){//没有增加条件
					sb.append(" "+c.getColumnName()+"=:"+c.getFieldName());
				}else{
					sb.append(" and "+c.getColumnName()+"=:"+c.getFieldName());
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 根据主键 组成查询条件 
	 * @param schema
	 * @return
	 */
	private static String getWhereSQLBySchemaInfo_novalue(SchemaInfo schema){
		StringBuffer sb =new StringBuffer();
		for (SchemaColumn c : schema.getColumns()) {
			if(c.isPrimaryKey()){
				if(sb.length()<1){//没有增加条件
					sb.append(" "+c.getColumnName()+"=?");
				}else{
					sb.append(" and "+c.getColumnName()+"=? ");
				}
			}
		}
		return sb.toString();
	}
	
	private static String getWhereSQLBySchemaInfo(SchemaInfo schema){
		StringBuffer sb =new StringBuffer();
		for (SchemaColumn c : schema.getColumns()) {
			if(c.isPrimaryKey()){
				if(sb.length()<1){//没有增加条件
					sb.append(" "+c.getColumnName()+"="+c.getFieldValue());
				}else{
					sb.append(" and "+c.getColumnName()+"="+c.getFieldValue());
				}
			}
		}
		return sb.toString();
	}
	
	public static String getOrderBySQLBySchemaInfo(SchemaInfo schema){
		StringBuffer sb =new StringBuffer();
		for (SchemaColumn c : schema.getColumns()) {
			if(c.isPrimaryKey()){
				sb.append(c.getColumnName()+",");
			}
		}
		if(schema.getColumns().size()==0){
			return null;
		}else{
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		
	}
	/**
	 * 获取查询条件数组
	 * @param schema
	 * @return
	 */
	private static String[] getWhereInfoBySchemaInfo(SchemaInfo schema){
		String[] wherebodys=new String[schema.getColumns().size()+1];
		for (int i=0;i<schema.getColumns().size();i++) {
			SchemaColumn schemaColumn=schema.getColumns().get(i);
			if(schemaColumn.getFieldClass().getSimpleName().contains("String")){
				wherebodys[i] =schemaColumn.getColumnName()+" like:"+schemaColumn.getFieldName();
			}else if(schemaColumn.getFieldClass().getSimpleName().contains("Timestamp")){
				//DATE_FORMAT(systemlog.CREATE_DATE,'%Y-%m-%d')>=:begintime
				wherebodys[i] ="DATE_FORMAT("+schemaColumn.getColumnName()+",'%Y-%m-%d')"+">=:"+schemaColumn.getFieldName()+"1";
				wherebodys[i+1] ="DATE_FORMAT("+schemaColumn.getColumnName()+",'%Y-%m-%d')"+"<=:"+schemaColumn.getFieldName()+"2";
				i++;
			}else{
				wherebodys[i] =schemaColumn.getColumnName()+" =:"+schemaColumn.getFieldName();
			}
			
		}
		return wherebodys;
	}
	/**
	 * 获取name 对应数据库的表字段
	 * @param schema
	 * @return
	 */
	public static Map<String,String> getMapperBySchemaInfo(SchemaInfo schema){
		Map<String, String> params=new HashMap<String, String>();
		for (SchemaColumn c : schema.getColumns()) {
			params.put(c.getColumnName(), c.getFieldName());
		}
		return params;
	}
	
	
}
