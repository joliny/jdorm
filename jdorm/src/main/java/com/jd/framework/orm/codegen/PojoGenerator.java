/**   
* @Title: PojoGenerator.java 
* @Package com.jd.framework.orm.codegen 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午10:52:28 
* @version V1.0   
*/ 
package com.jd.framework.orm.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
/**
 * @author liubing1@jd.com
 *
 */
public class PojoGenerator {
	private Connection con;
	private String destfile = "D:/work/workplace/jdorm/src/test/java";
	private File destination;
	private String packageName = "test.jd.framework.orm.entity";
	private String driver= "com.mysql.jdbc.Driver";
	private String url= "jdbc:mysql://127.0.0.1/test";
	private String username= "root";
	private String password= "client";
	private String prefix;
	private boolean genJsonAnnotations = false;

	public PojoGenerator(String driver, String url, String username, String password, String packageName) {
		this.driver =  driver;
		this.url =  url;
		this.username =  username;
		this.password =  password;
		this.packageName =  packageName;
		this.destination = new File(this.destfile);
	}

	public PojoGenerator(String driver, String url, String username, String password, String packageName, String destination) {
		this.driver =  driver;
		this.url =  url;
		this.username =  username;
		this.password =  password;
		this.packageName =  packageName;
		this.destfile =  destination;
		this.destination = new File(this.destfile);
	}
	
	/**
	 * get database type
	 * @return
	 */
	public DataBaseType guessDataBaseType() {
		String dbDriver = driver.toLowerCase();
		if (dbDriver.indexOf("oracledriver") >= 0) {
			return DataBaseType.ORACLE;
		} else if (dbDriver.indexOf("db2driver") >= 0) {
			return DataBaseType.DB2;
		} else if (dbDriver.indexOf("postgresql") >= 0) {
			return DataBaseType.PSQL;
		} else if (dbDriver.indexOf("sqlserverdriver") >= 0) {
			return DataBaseType.SQLSERVER;
		} else if (dbDriver.indexOf("mysql") >= 0) {
			return DataBaseType.MYSQL;
		} else if (dbDriver.indexOf("h2") >= 0) {//org.h2.Driver
			return DataBaseType.H2;
		} else if (dbDriver.indexOf("sqlite") >= 0) {//org.sqlite.JDBC 
			return DataBaseType.SQLITE;
		} else if (dbDriver.indexOf("hsqldb") >= 0) {
			return DataBaseType.HSQL;
		} else if (dbDriver.indexOf("derby") >= 0) {
			return DataBaseType.DERBY;
		} else if (dbDriver.indexOf("firebirdsql") >= 0) {
			return DataBaseType.FIREBIRD;
		} else if (dbDriver.indexOf("interbase") >= 0) {
			return DataBaseType.INTERBASE;
		} else if (dbDriver.indexOf("informix") >= 0) {
			return DataBaseType.INFORMIX;
		} else if (dbDriver.indexOf("ingres") >= 0) {
			return DataBaseType.INGRES10;
		} else if (dbDriver.indexOf("rdms2200") >= 0) {
			return DataBaseType.RDMS2200;
		} else if (dbDriver.indexOf("timesten") >= 0) {
			return DataBaseType.TIMESTEN;
		} else if (dbDriver.indexOf("mariadb") >= 0) {
			return DataBaseType.MARIADB;
		} else if (dbDriver.indexOf("sybase") >= 0) {
			return DataBaseType.SYBASE;
		}
		return DataBaseType.OTHER;
	}

	/**
	 * @return the destfile
	 */
	public String getDestfile() {
		return destfile;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix.toUpperCase();
	}

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		if(con == null) {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password); 
		}
		return con;
	}

	public void closeConnection() {
		if(this.con != null){
			try {
				this.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.con = null;
		}
	}

	/**
	 * 生成指定的数据库里所有的表对应的POJO类
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public void createDatabaseEntities() {
		ResultSet rs = null;
		try{
			Connection conn = getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			rs = dbMeta.getTables(null, this.username.toUpperCase(), "%", new String[] { "TABLE", "VIEW" });

			File folder = new File(this.destination.getAbsolutePath() + 
					File.separatorChar + 
					this.packageName.replace('.', File.separatorChar));

			if (!folder.exists()) {
				folder.mkdirs();
			}

			while (rs.next()) {
				String tablename = rs.getString(3);
				String remark = null;
				try{
					remark = rs.getString(5);
				}catch(Exception e) {
				}
				String className = tablename;
				Set<Member> members = fetchMembers(conn, className);
				if(this.prefix != null) {
					if(className.toUpperCase().startsWith(this.prefix)){
						className = className.substring(this.prefix.length());
					}
				}
				if(remark == null){
					remark = tablename;
				}
				className = GenUtil.capitalize(className, GenUtil.PASCAL_CASE);
				String javaCode = generateJavaCode1(tablename, remark, className, members, null,"test","test");
				File javaFile = new File(folder, className + "Entity.java");
				save(javaFile, javaCode);
			}
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			closeConnection();
		}
	}
	
	/**
	 * 生成指定表对应的POJO类
	 * @param tablename
	 * @param idgenerator 主键生成方式 {@link KeyGenertator}
	 */
	public void createDatabaseEntities(String tablename, String idgenerator,String writedatasource,String readDatasource) {
		ResultSet rs = null;
		try{
			File folder = new File(this.destination.getAbsolutePath() + 
					File.separatorChar + 
					this.packageName.replace('.', File.separatorChar));

			if (!folder.exists()) {
				folder.mkdirs();
			}

			Connection conn = getConnection();
			String className = tablename;
			Set<Member> members = fetchMembers(conn, className);
			if(this.prefix != null) {
				if(className.toUpperCase().startsWith(this.prefix)){
					className = className.substring(this.prefix.length());
				}
			}
			className = GenUtil.capitalize(className, GenUtil.PASCAL_CASE);
			String remark = null;
			ResultSet tableSet = null;
			try{
				tableSet = conn.getMetaData().getTables(null, this.username.toUpperCase(), tablename,
						new String[] { "TABLE" });
				while (tableSet.next()) {
					remark = tableSet.getString(5);
					break;
				}
			}catch(Exception e) {
			} finally {
				if(tableSet != null) {
					try{
						tableSet.close();
					}catch(Exception sqle) {
					}
					tableSet = null;
				}
			}
			if(remark == null) {
				remark = tablename;
			}
			String javaCode = generateJavaCode1(tablename, remark, className, members, idgenerator,writedatasource,readDatasource);
			File javaFile = new File(folder, className + "Entity.java");
			save(javaFile, javaCode);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			closeConnection();
		}
	}

	/**
	 * fetch table meta info 2 Member set
	 * @param conn
	 * @param tableName
	 * @return
	 */
	private Set<Member> fetchMembers(Connection conn, String tableName) {
		Set<Member> members = new TreeSet<Member>();
		ResultSet rs = null;
		try {
			DataBaseType dataBaseType = guessDataBaseType();
			Set<String> pkfieldSet = new HashSet<String>();
			Map<String, String> fkfieldMap= new HashMap<String, String>();//table.field
			boolean bupper = true;
			DatabaseMetaData dbmd = null;
			if(DataBaseType.SQLSERVER == dataBaseType) {
				bupper = false;
				String sql = "SELECT column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE OBJECTPROPERTY(OBJECT_ID(constraint_name), 'IsPrimaryKey') = 1 AND table_name = '"+tableName+"'";
				PreparedStatement stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				while (rs.next()) {
					String pk = rs.getString(1);
					if(pk != null){
						pkfieldSet.add(pk);
					}
				}
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				StringBuilder fsql = new StringBuilder("select t1.name, t2.rtableName,t2.name from");
				fsql.append(" (select col.name, f.constid as temp  from sys.syscolumns col,sys.sysforeignkeys f where ");
				fsql.append(" f.fkeyid=col.id  and f.fkey=col.colid  and f.constid in");
				fsql.append(" ( select distinct(id) from sys.sysobjects where OBJECT_NAME(parent_obj)='").append(tableName).append("'");
				fsql.append(" and xtype='F') ) as t1 , (");
				fsql.append(" select OBJECT_NAME(f.rkeyid) as rtableName,col.name, f.constid as temp  from ");
				fsql.append(" sys.syscolumns col, sys.sysforeignkeys f");
				fsql.append(" where f.rkeyid=col.id  and f.rkey=col.colid ");
				fsql.append(" and f.constid in (select distinct(id) from sys.sysobjects");
				fsql.append(" where OBJECT_NAME(parent_obj)='").append(tableName).append("' and xtype='F') ) as t2 where t1.temp=t2.temp");
				stmt = conn.prepareStatement(fsql.toString());
				rs = stmt.executeQuery();
				while (rs.next()) {
					String colname = rs.getString(1);
					String reftbl = rs.getString(2);
					String reffield = rs.getString(3);
					if(colname != null) {
						fkfieldMap.put(colname, String.format("%s.%s", reftbl, reffield));
					}
				}
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
			}else{
				if(DataBaseType.MARIADB == dataBaseType) {
					String catalog = conn.getCatalog();
					String sql = "SELECT K.COLUMN_NAME,K.REFERENCED_TABLE_NAME,K.REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE K WHERE REFERENCED_TABLE_NAME IS NOT NULL AND TABLE_SCHEMA='%s' AND TABLE_NAME='%s'";
					PreparedStatement stmt = conn.prepareStatement(String.format(sql, catalog, tableName));
					rs = stmt.executeQuery();
					while (rs.next()) {
						String colname = rs.getString(1);
						String reftbl = rs.getString(2);
						String reffield = rs.getString(3);
						if(colname != null) {
							fkfieldMap.put(colname.toUpperCase(), String.format("%s.%s", reftbl, reffield));
						}
					}
					rs.close();
					rs = null;
					stmt.close();
					stmt = null;
				}else if(DataBaseType.MYSQL == dataBaseType) {
					String catalog = conn.getCatalog();
					String sql = "SELECT K.COLUMN_NAME,K.REFERENCED_TABLE_NAME,K.REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE K WHERE REFERENCED_TABLE_NAME IS NOT NULL AND TABLE_SCHEMA='%s' AND TABLE_NAME='%s'";
					PreparedStatement stmt = conn.prepareStatement(String.format(sql, catalog, tableName));
					rs = stmt.executeQuery();
					while (rs.next()) {
						String colname = rs.getString(1);
						String reftbl = rs.getString(2);
						String reffield = rs.getString(3);
						if(colname != null) {
							fkfieldMap.put(colname.toUpperCase(), String.format("%s.%s", reftbl, reffield));
						}
					}
					rs.close();
					rs = null;
					stmt.close();
					stmt = null;
				}else if(DataBaseType.ORACLE == dataBaseType) {
					StringBuilder sb = new StringBuilder("SELECT B.COLUMN_NAME,D.TABLE_NAME AS REF_TABLE_NAME,D.COLUMN_NAME AS REF_COLUMN_NAME FROM USER_CONSTRAINTS A");
					sb.append(" INNER JOIN USER_CONS_COLUMNS B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME");
					sb.append(" LEFT JOIN USER_CONS_COLUMNS D ON A.R_CONSTRAINT_NAME = D.CONSTRAINT_NAME");
					sb.append(" WHERE A.CONSTRAINT_TYPE = 'R' AND A.TABLE_NAME='").append(tableName).append('\'');
					PreparedStatement stmt = conn.prepareStatement(sb.toString());
					rs = stmt.executeQuery();
					while (rs.next()) {
						String colname = rs.getString(1);
						String reftbl = rs.getString(2);
						String reffield = rs.getString(3);
						if(colname != null) {
							fkfieldMap.put(colname.toUpperCase(), String.format("%s.%s", reftbl, reffield));
						}
					}
					rs.close();
					rs = null;
					stmt.close();
					stmt = null;
				}else if(DataBaseType.DB2 == dataBaseType) {
					String sql = "SELECT R.FK_COLNAMES,R.REFTABNAME,R.PK_COLNAMES FROM SYSCAT.REFERENCES R WHERE R.TABNAME='%s'";
					PreparedStatement stmt = conn.prepareStatement(String.format(sql, tableName));
					rs = stmt.executeQuery();
					while (rs.next()) {
						String colname = rs.getString(1);
						String reftbl = rs.getString(2);
						String reffield = rs.getString(3);
						if(colname != null) {
							fkfieldMap.put(colname.trim().toUpperCase(), String.format("%s.%s", reftbl.trim(), reffield.trim()));
						}
					}
					rs.close();
					rs = null;
					stmt.close();
					stmt = null;
				}
				dbmd =  conn.getMetaData();
				rs = dbmd.getPrimaryKeys(null,username.toUpperCase(),tableName);
				while (rs.next()) {
					String pk = rs.getString(4);
					if(pk != null){
						pk = pk.toUpperCase();
						pkfieldSet.add(pk);
					}
				}
				rs.close();
				rs = null;
			}
			if(dbmd == null) {
				dbmd = conn.getMetaData();
			}
			Map<String, String> fieldRemarks = new HashMap<String, String>();
			ResultSet columnSet = null;
			try{
				columnSet = dbmd.getColumns(null, username.toUpperCase(),tableName, "%");
				while (columnSet.next()) {
					String columnName = columnSet.getString("COLUMN_NAME");
					String columnComment = columnSet.getString("REMARKS");
					fieldRemarks.put(columnName, columnComment);
				}
			}catch(Exception e) {
			}finally{
				if(columnSet != null) {
					try{
						columnSet.close();
					}catch(Exception sqle) {
					}
					columnSet = null;
				}
			}
			String sql = "SELECT * FROM " + tableName + " WHERE 1=2";
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			for (int col = 1; col <= rsMeta.getColumnCount(); col++) {
				Member member = new Member();
				String name = rsMeta.getColumnName(col);
				String remark = fieldRemarks.get(name);
				if(remark != null) {
					member.setRemark(remark);
				}
				if(bupper){
					if(pkfieldSet.contains(name.toUpperCase())){
						member.setPk(true);//PK
					}
					member.setColname(name.toUpperCase());
				}else{
					if(pkfieldSet.contains(name)){
						member.setPk(true);//PK
					}
					member.setColname(name);
				}
				String ref = fkfieldMap.get(member.getColname());
				if(ref != null) {
					member.setFk(true);
					member.setReffield(ref);
				}
				name = GenUtil.capitalize(name, GenUtil.CAMEL_CASE);
				member.setName(name);
				member.setSize(rsMeta.getPrecision(col));
//				member.setWritable(rsMeta.isWritable(col));
				member.setWritable(true);
				member.setNullable((rsMeta.isNullable(col) == 1));
				member.setColtypename(rsMeta.getColumnTypeName(col));
				//rsMeta.getColumnTypeName(column)
				member.setColtype(rsMeta.getColumnType(col));
				if(member.getColtype() == Types.CLOB
						|| member.getColtype() == Types.BLOB
						|| member.getColtype() == Types.NCLOB){
					member.setType(byte[].class);
				}else{
					String typeName = null;
					if(DataBaseType.SQLITE == dataBaseType) {//SQLite
						typeName = getSQLiteColumnClass(member.getColtype());
					} else {
						typeName = rsMeta.getColumnClassName(col);
					}
					typeName = change2properTypeName(typeName, member.isPk(), member.isFk());
					member.setType(getClass(typeName));
				}
				members.add(member);
			}
			stmt.close();
			stmt = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
		}

		return members;
	}
	
//	SQL type	Java Type
//	CHAR	    String
//	VARCHAR	    String
//	LONGVARCHAR	String
//	NUMERIC	    java.math.BigDecimal
//	DECIMAL	    java.math.BigDecimal
//	BIT	        boolean
//	TINYINT	    byte
//	SMALLINT	short
//	INTEGER	    int
//	BIGINT	    long
//	REAL	    float
//	FLOAT	    double
//	DOUBLE	    double
//	BINARY	    byte[]
//	VARBINARY	byte[]
//	LONGVARBINARY	byte[]
//	DATE	    java.sql.Date
//	TIME	    java.sql.Time
//	TIMESTAMP	java.sql.Timestamp
	private String getSQLiteColumnClass(int columnType) {
		String colClass = "java.lang.Object";
		switch (columnType) {
		case Types.BOOLEAN:
			colClass = "java.lang.Boolean";
			break;
		case Types.TINYINT:
			colClass = "java.lang.Byte";
			break;
		case Types.SMALLINT:
			colClass = "java.lang.Short";
			break;
		case Types.BIGINT:
			colClass = "java.lang.Long";
			break;
		case Types.DATE:
			colClass = "java.sql.Date";
			break;
		case Types.INTEGER:
			colClass = "java.lang.Integer";
			break;
		case Types.DECIMAL:
			colClass = "java.math.BigDecimal";
			break;
		case Types.DOUBLE:
			colClass = "java.lang.Double";
			break;
		case Types.NUMERIC:
			colClass = "java.math.BigDecimal";
			break;
		case Types.REAL:
			colClass = "java.lang.Float";
			break;
		case Types.FLOAT:
			colClass = "java.lang.Float";
			break;
		case Types.CHAR:
			colClass = "java.lang.String";
			break;
		case Types.CLOB:
			colClass = "java.sql.Clob";
			break;
		case Types.VARCHAR:
			colClass = "java.lang.String";
			break;
		case Types.BINARY:
			colClass = "byte[]";
			break;
		case Types.BLOB:
			colClass = "java.sql.Blob";
			break;
		default:
			break;
		}
		return colClass;
	}
	
	private Class<?> getClass(String typeName) {
		if(typeName.endsWith("[]")) {
			if(typeName.equals("byte[]")) {
				return byte[].class;
			} 
		}
		try {
			return Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Object.class;
	}

	/**
	 * 转换成合适的类型
	 * @param typeName
	 * @param pk
	 * @return
	 */
	protected String change2properTypeName(String typeName, boolean pk, boolean fk) {
		String name = typeName;
		if(typeName.equals("oracle.sql.TIMESTAMP")) {
			name = "java.sql.Timestamp";
		} else if(typeName.equals("java.math.BigDecimal")) {
			if(pk || fk) {
				name = "java.lang.Long";
			} else {
				name = "java.lang.Integer";
			}
		} else if(typeName.equals("java.sql.Date")) {
			name = "java.util.Date";
		} else if(typeName.equals("java.sql.Clob")) {
			name = "java.lang.String";//"byte[]";
		} else if(typeName.equals("java.sql.Blob")) {
			name = "byte[]";//"java.sql.Blob";
		}
		return name;
	}

	/**
	 * generate POJO java source contents
	 * @param className
	 * @param members
	 * @param idgenerator 主键生成方式 {@link KeyGenertator}
	 * @return
	 */
	private String generateJavaCode(String tablename, String tableRemark, String className, Set<Member> members, String idgenerator) {
		TabStack code = new TabStack();
		addPackage(code, className);
		addimports(code, members);
		addClass(code, tablename, tableRemark, className, members, idgenerator);
		return code.toString();
	}
	
	private String generateJavaCode1(String tablename, String tableRemark, String className, Set<Member> members, String idgenerator,String writeDatasource,String readDatasource) {
		TabStack code = new TabStack();
		addPackage(code, className);
		addimports(code, members);
		addClass1(code, tablename, tableRemark, className, members, idgenerator,writeDatasource,readDatasource);
		return code.toString();
	}

	/**
	 * add package
	 * @param code
	 */
	private void addPackage(TabStack code, String className) {
		code.append("/*");
		code.appendEOL();
		code.append(" * 文件：");
		code.append(this.packageName);
		code.append(".");
		code.append(className);
		code.append(".java");
		code.appendEOL();
		code.append(" * 日 期：");
		code.append(new Date().toString());
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		
		code.append("package ");
		code.append(this.packageName);
		code.append(";");
		code.appendEOL();
		code.appendEOL();
	}

	/**
	 * add needed import classes
	 * @param code
	 * @param members
	 */
	private void addimports(TabStack code, Set<Member> members) {
		Set<String> imports = new TreeSet<String>();
		imports.add("import java.io.Serializable;" + GenUtil.LINE_END);
		imports.add("import com.jd.framework.orm.entity.BaseEntity;" + GenUtil.LINE_END);
		
		imports.add("import java.util.ArrayList;" + GenUtil.LINE_END);
		imports.add("import java.util.List;" + GenUtil.LINE_END);
		imports.add("import com.jd.framework.orm.schema.SchemaColumn;" + GenUtil.LINE_END);
		imports.add("import com.jd.framework.orm.schema.SchemaInfo;" + GenUtil.LINE_END);
		
		imports.add("import com.jd.framework.orm.annotation.Table;" + GenUtil.LINE_END);
		imports.add("import com.jd.framework.orm.annotation.Column;" + GenUtil.LINE_END);
		for (Member member : members) {
			imports.add(member.getImport());
		}
		for (String imp : imports) {
			code.append(imp);
		}
		code.appendEOL();
	}
	
	/**
	 * add class file contents
	 * @param code
	 * @param className
	 * @param members
	 * @param idgenerator
	 */
	private void addClass1(TabStack code, String tablename, String tableRemark, String className, Set<Member> members, String idgenerator,String writerDatasource,String readDatasource) {
		code.append("/**");
		code.appendEOL();
		code.append(" *");
		code.appendEOL();
		code.append(" * ");
		code.append(tableRemark);
		code.appendEOL();
		code.append(" * this file is generated by the jdorm pojo tools.");
		code.appendEOL();
		code.append(" *");
		code.appendEOL();
		code.append(" * @author <a href=\"mailto:liubingsmile@gmail.com\">liubing</a>");
		code.appendEOL();
		code.append(" * @version 1.0.0");
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		
		code.append("@Table(name = \"");
		code.append(tablename+"\",");
		code.append("readschema=\""+readDatasource+"\",");
		code.append("writeschema=\""+writerDatasource);
		//String keyorder = null;
//		int count = 0;
//		for (Member member : members) {
//			if(member.isPk()){
//				if(keyorder == null){
//					keyorder = member.getName();
//				}else{
//					keyorder += "," + member.getName();
//				}
//				count ++;
//			}
//		}
		
		code.append("\")");
		code.appendEOL();
		
		code.append("public class ");
		code.append(className+"Entity");
		code.append(" extends BaseEntity");
		code.append(" implements Serializable {");
		code.push();
		code.appendEOL();

		addSerialVersionUID(code, className, members);
		code.appendEOL();

		//addStaticPorps(code, members);
		code.appendEOL();
		
		boolean haspk = false;
		boolean hasnonnull = false;
		//pk first
		for (Member member : members) {
			if(member.isPk()){
				member.addField(code, genJsonAnnotations);
				haspk = true;
			} else if(!member.isNullable()) {
				hasnonnull = true;
			}
		}
		//then other fields
		for (Member member : members) {
			if(!member.isPk()){
				member.addField(code, genJsonAnnotations);
			}
		}
		code.appendEOL();

		//add default constructor
		code.append("public ");
		code.append(className+"Entity");
		code.append("() {");
		code.push();
		code.append("super();");
		code.pop();
		code.append("}");
		code.appendEOL(2);
		
		for (Member member : members) {
			member.addAccessors(code);
			code.appendEOL(2);
		}

		if(haspk) {
			addEquals(code, className, members);
			code.appendEOL();

			addHashCode(code, className, members);
		}
		code.appendEOL(2);
		
		addSchema(code, tablename, className, members,  writerDatasource, readDatasource);
		code.appendEOL(2);
		toJsonObject(code, className, members);
		code.pop();
		code.append("}");
	}
	
	public void toJsonObject(TabStack code,String className,Set<Member> members){
		code.append("/* (non-Javadoc)");
		code.appendEOL();
		code.append(" * @see "+packageName+"."+className+"#toJsonObject()");
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		code.append("@Override");
		code.appendEOL();
		code.append("public String toJsonObject() {");
		code.appendEOL();
		code.append("return \"{");
		int i=0;
		for(Member member:members){
			if(i==members.size()-1){
				code.append("'"+member.getName()+"':'\"+"+member.getName()+"+\"'");
			}else{
				code.append("'"+member.getName()+"':'\"+"+member.getName()+"+\"',");
			}
			i++;
		}
		code.append("}\";");
		code.appendEOL();
		code.append("}");
	}
	
	
	private void addSchema(TabStack code, String tablename, String className, Set<Member> members, String writerDatasource,String readDatasource){
		code.append("/* (non-Javadoc)");
		code.appendEOL();
		code.append(" * @see "+packageName+"."+className+"#getSchemaInfo()");
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		code.append("@Override");
		code.appendEOL();
		code.append("public SchemaInfo getSchemaInfo() {");
		code.appendEOL();
		code.append("SchemaInfo schemaInfo=new SchemaInfo();");
		code.appendEOL();
		code.append("schemaInfo.setTableClass("+className+"Entity.class);");
		code.appendEOL();
		code.append("schemaInfo.setReadDataBase(\""+readDatasource+"\");");
		code.appendEOL();
		code.append("schemaInfo.setWriteDataBase(\""+writerDatasource+"\");");
		code.appendEOL();
		code.append("schemaInfo.setTableName(\""+tablename+"\");");
		code.appendEOL();
		code.append("List<SchemaColumn> schemaColumns=new ArrayList<SchemaColumn>();");
		code.appendEOL();
		int i=0;
		for(Member member:members){
			if(i==0){
				code.append("SchemaColumn schemaColumn=new SchemaColumn();");
			}else{
				code.append("schemaColumn=new SchemaColumn();");
			}
			
			code.appendEOL();
			code.append("schemaColumn.setColumnName(\""+member.getName()+"\");");
			code.appendEOL();
			code.append("schemaColumn.setFieldClass("+member.getType().getName()+".class);");
			code.appendEOL();
			code.append("schemaColumn.setFieldName(\""+member.getName()+"\");");
			code.appendEOL();
			code.append("schemaColumn.setFieldValue("+member.getName()+");");
			code.appendEOL();
			code.append("schemaColumn.setIstransient(false);");
			code.appendEOL();
			code.append("schemaColumn.setNullable("+member.isNullable()+");");
			code.appendEOL();
			code.append("schemaColumn.setPrimaryKey("+member.isPk()+");");
			code.appendEOL();
			code.append("schemaColumn.setTypeName(\""+member.getColtypename()+"\");");
			code.appendEOL();
			code.append("schemaColumn.setTypeLength("+member.getSize()+");");
			code.appendEOL();
			code.append("schemaColumn.setTypeScale(0);");
			code.appendEOL();
			code.append("schemaColumns.add(schemaColumn);");
			code.appendEOL();
			i++;
		}
		code.append("schemaInfo.setColumns(schemaColumns);");
		code.appendEOL();
		code.append("return schemaInfo;");
		code.appendEOL();
		code.append("}");
		code.appendEOL(2);
	}
	
	/**
	 * add class file contents
	 * @param code
	 * @param className
	 * @param members
	 * @param idgenerator
	 */
	private void addClass(TabStack code, String tablename, String tableRemark, String className, Set<Member> members, String idgenerator) {
		code.append("/**");
		code.appendEOL();
		code.append(" *");
		code.appendEOL();
		code.append(" * ");
		code.append(tableRemark);
		code.appendEOL();
		code.append(" * this file is generated by the jdorm pojo tools.");
		code.appendEOL();
		code.append(" *");
		code.appendEOL();
		code.append(" * @author <a href=\"mailto:liubingsmile@gmail.com\">liubing</a>");
		code.appendEOL();
		code.append(" * @version 1.0.0");
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		
		code.append("@Table(name = \"");
		code.append(tablename);
		String keyorder = null;
		int count = 0;
		for (Member member : members) {
			if(member.isPk()){
				if(keyorder == null){
					keyorder = member.getName();
				}else{
					keyorder += "," + member.getName();
				}
				count ++;
			}
		}
		if(count > 1) {
			code.append("\", keyOrder = \"");
			code.append(keyorder);
		}
		if(idgenerator != null) {
			code.append("\", keyGenerator = \"");
			code.append(idgenerator);
		}
		code.append("\")");
		code.appendEOL();
		
		code.append("public class ");
		code.append(className);
		code.append(" extends BaseEntity");
		code.append(" implements Serializable {");
		code.push();
		code.appendEOL();

		addSerialVersionUID(code, className, members);
		code.appendEOL();

		//addStaticPorps(code, members);
		code.appendEOL();
		
		boolean haspk = false;
		boolean hasnonnull = false;
		//pk first
		for (Member member : members) {
			if(member.isPk()){
				member.addField(code, genJsonAnnotations);
				haspk = true;
			} else if(!member.isNullable()) {
				hasnonnull = true;
			}
		}
		//then other fields
		for (Member member : members) {
			if(!member.isPk()){
				member.addField(code, genJsonAnnotations);
			}
		}
		code.appendEOL();

		//add default constructor
		code.append("public ");
		code.append(className);
		code.append("() {");
		code.push();
		code.append("super();");
		code.pop();
		code.append("}");
		code.appendEOL(2);
		
		//
		boolean sameconstructor = false;
		if(haspk){
			StringBuffer argFieldBuf = new StringBuffer();
			String notnullArgField = "";
			code.append("public ");
			code.append(className);
			code.append("(");
			int idx = 0;
			for (Member member : members) {
				if(member.isPk()) {
					if(idx != 0){
						code.append(", ");
					}
					code.append(member.getType().getSimpleName());
					argFieldBuf.append(member.getType().getSimpleName()).append(", ");
					code.append(" ");
					code.append(member.decapitalize(member.getName()));
					idx ++;
				}
				if(!member.isNullable() && !member.isPk()) {
					notnullArgField += member.getType().getSimpleName() + ", ";
				}
			}
			if(notnullArgField.equals(argFieldBuf.toString())) {
				sameconstructor = true;
			}
			code.append(") {");
			code.push();
			int argcount = 0;
			for (Member member : members) {
				if(member.isPk()) {
					argcount ++;
					code.append("this.");
					code.append(member.decapitalize(member.getName()));
					code.append(" = ");
					code.append(member.decapitalize(member.getName()));
					code.append(";");
					if(argcount == idx){
						code.pop();
					} else {
						code.appendEOL();
					}
				}
			}
			code.append("}");
			code.appendEOL(2);
		}
		
		//not null 
		if(hasnonnull && !sameconstructor) {
			code.append("public ");
			code.append(className);
			code.append("(");
			int idx = 0;
			for (Member member : members) {
				if(!member.isNullable() && !member.isPk()) {
					if(idx != 0){
						code.append(", ");
					}
					code.append(member.getType().getSimpleName());
					code.append(" ");
					code.append(member.decapitalize(member.getName()));
					idx ++;
				}
			}
			code.append(") {");
			code.push();
			int argcount = 0;
			for (Member member : members) {
				if(!member.isNullable() && !member.isPk()) {
					argcount ++;
					code.append("this.");
					code.append(member.decapitalize(member.getName()));
					code.append(" = ");
					code.append(member.decapitalize(member.getName()));
					code.append(";");
					if(argcount == idx){
						code.pop();
					} else {
						code.appendEOL();
					}
				}
			}
			code.append("}");
			code.appendEOL(2);
		}
		
		for (Member member : members) {
			member.addAccessors(code);
			code.appendEOL(2);
		}

		if(haspk) {
			addEquals(code, className, members);
			code.appendEOL();

			addHashCode(code, className, members);
		}
		code.appendEOL(2);
		
		
		code.pop();
		code.append("}");
	}
	
/*	private void addStaticPorps(TabStack code, Set<Member> members) {
		for(Member member : members){
			code.append("public static String PROP_");
			code.append(member.getColname().toUpperCase());
//			code.append(" = \""+member.getName()+"\";");
			code.append(" = \""+member.getColname()+"\";");
			code.appendEOL();
		}
	}*/

	/**
	 * add serialVersionUID by default 1L
	 * @param code
	 * @param className
	 * @param members
	 */
	private void addSerialVersionUID(TabStack code, String className, Set<Member> members) {
		code.append("private static final long serialVersionUID = 1L;");
		code.appendEOL();
	}

	/**
	 * add hashCode method
	 * @param code
	 * @param className
	 * @param members
	 */
	private void addHashCode(TabStack code, String className, Set<Member> members) {
		code.append("/* (non-Javadoc)");
		code.appendEOL();
		code.append(" * @see java.lang.Object#hashCode()");
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		
		code.append("@Override");
		code.appendEOL();
		code.append("public int hashCode() {");
		code.push();

		code.append("final int prime = 31;");
		code.appendEOL();
		code.append("int result = 1;");
		code.appendEOL();
		for (Member member : members) {
			if(member.isPk()) {
				//result = prime * result + ((id == null) ? 0 : id.hashCode());
				code.append("result = prime * result + ((");
				code.append(member.getName());
				code.append(" == null) ? 0 : ");
				code.append(member.getName());
				code.append(".hashCode());");
				code.appendEOL();
			}
		}

		code.append("return result;");
		code.pop();
		code.append("}");
		code.appendEOL();
	}

	/**
	 * add equals method
	 * @param code
	 * @param className
	 * @param members
	 */
	private void addEquals(TabStack code, String className, Set<Member> members) {
		code.append("/* (non-Javadoc)");
		code.appendEOL();
		code.append(" * @see java.lang.Object#equals(java.lang.Object)");
		code.appendEOL();
		code.append(" */");
		code.appendEOL();
		
		code.append("@Override");
		code.appendEOL();
		code.append("public boolean equals(Object o) {");
		code.push();

		code.append("if ((o == null) || !(o instanceof ");
		code.append(className+"Entity");
		code.append(")) {");
		code.push();
		code.append("return false;");
		code.pop();
		code.append("}");
		code.appendEOL();

		code.append(className+"Entity");
		code.append(" other = (");
		code.append(className+"Entity");
		code.append(")");
		code.append("o;");
		code.appendEOL();

		for (Member member : members) {
			if(member.isPk()) {
				//if (id == null) {
				//	if (other.id != null)
				//		return false;
				//} else if (!id.equals(other.id))
				//	return false;
				code.append("if (null == this.");
				code.append(member.getName());
				code.append(") {");
				code.push();
				code.append("if (other.");
				code.append(member.getName());
				code.append(" != null)");
				code.push();
				code.append("return false;");
				code.pop(2);
				code.append("} else if (!this.");
				code.append(member.getName());
				code.append(".equals(other.");
				code.append(member.getName());
				code.append("))");
				code.push();
				code.append("return false;");
				code.pop();
			}
		}
		code.append("return true;");
		code.pop();
		code.append("}");
		code.appendEOL();
	}

	/**
	 * save java source file
	 * @param file
	 * @param content
	 */
	private void save(File file, String content) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
			System.out.println("\"" + file.getName() + "\" created successfully!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * @return the genJsonAnnotations
	 */
	public boolean isGenJsonAnnotations() {
		return genJsonAnnotations;
	}

	/**
	 * @param genJsonAnnotations the genJsonAnnotations to set
	 */
	public void setGenJsonAnnotations(boolean genJsonAnnotations) {
		this.genJsonAnnotations = genJsonAnnotations;
	}

	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1/test";
		String username = "root";
		String password = "client";
		String packageName = "test.jd.framework.orm.entity";
		PojoGenerator generator = new PojoGenerator(driver, url, username, password, packageName);
		generator.setPrefix("UUM_");
		generator.createDatabaseEntities();
		//generator.createDatabaseEntities("demo", null,"test","test");
	}
}
