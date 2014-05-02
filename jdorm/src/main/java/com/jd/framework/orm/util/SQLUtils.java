package com.jd.framework.orm.util;


import java.util.Map;
/**
 * SQL语句组织及相关处理的辅助类，通常用于组织完整的SQL语句以传递给DAO对象进行数据存取操作。
 * 
 * @author liubing
 * 
 */
public class SQLUtils {

	/**
	 * 将一个查询记录的select语句，自动转变成查询符合条件的记录数量的 select count语句
	 * 
	 * @param originalHql
	 *            Have a form of "select ... from ... ..." or "from ..."
	 * 
	 * @param distinctName
	 *            用做count(distinctName)，可以为null
	 * 
	 * @return transform to "select count(*) from ... ..."
	 */
	public static String generateCountHql(String originalHql,
			String distinctName) {

		String loweredOriginalHql = originalHql.toLowerCase();
		int beginPos = loweredOriginalHql.indexOf("from");
		if (beginPos == -1) {
			throw new IllegalArgumentException("not a valid hql string");
		}
		String countField = null;
		if (distinctName != null) {
			countField = "distinct " + distinctName;
		} else {
			countField = "*";
		}

		return "select count("
				+ countField
				+ ")"
				+ originalHql.substring(beginPos).replaceAll("join fetch ",
						"join ");
	}

	/**
	 * 组织并返回完整的HQL语句。
	 * <p>
	 * <b>注意：</b> 通常参数whereBodies与params对应出现，。
	 * 
	 * @param queryFields
	 *            HQL中的返回字段指定语句，允许null值，即获取整个对象。
	 * @param fromJoinSubClause
	 *            HQL中的From子句及连接查询语句,不允许null值
	 * @param whereBodies
	 *            条件语句的数组，数组元素通常写为"prop1=:prop1Key"
	 * @param orderField
	 *            排序条件
	 * @param orderDirection
	 *            排序方向，ASC升序，DESC降序
	 * @param params
	 *            对应于whereBodies的参数值，map元素通常有如prop1Key=prop1Value的值。
	 * @return 完整的HQL语句
	 */
	public static String generateHql(final String queryFields,
			final String fromJoinSubClause, final String[] whereBodies,
			final String orderField, final String orderDirection,
			final Map<String, ?> params) {
		//Assert.notNull(fromJoinSubClause);
		StringBuffer sb = new StringBuffer();
		if(queryFields!=null){
			sb.append(queryFields).append(" ");
		}
		sb.append(fromJoinSubClause);
		sb.append(" ").append(generateHqlWhereClause(whereBodies, params));
		sb.append(" ").append(
				generateHqlOrderClause(orderField, orderDirection));
		String finalHql = sb.toString();

		return finalHql;
	}
	/**
	 * 刘兵  监测计划
	 * @param queryFields
	 * @param fromJoinSubClause
	 * @param whereBodies
	 * @param orderField
	 * @param orderDirection
	 * @param params
	 * @param or
	 * @return
	 */
	public static String generateHqlOr(final String queryFields,
			final String fromJoinSubClause, final String[] whereBodies,
			final String orderField, final String orderDirection,
			final Map<String, ?> params,String or) {
		//Assert.notNull(fromJoinSubClause);
		StringBuffer sb = new StringBuffer();
		if(queryFields!=null){
			sb.append(queryFields).append(" ");
		}
		sb.append(fromJoinSubClause);
		sb.append(" ").append(generateHqlWhereClause(whereBodies, params));
		sb.append(" ").append(or);
		sb.append(" ").append(
				generateHqlOrderClause(orderField, orderDirection));
		String finalHql = sb.toString();

		return finalHql;
	}
	
	/**
	 * 修改人：CMM
	 * Team模块用
	 * 注：此函数就是为了调用generateHqlWhereAppendBr方法（在where后加入了左小括号）
	 * @return 完整的HQL语句
	 */
	public static String generateHqlWhereAppendBr(final String queryFields,
			final String fromJoinSubClause, final String[] whereBodies,
			final String orderField, final String orderDirection,
			final Map<String, ?> params) {
		//Assert.notNull(fromJoinSubClause);
		StringBuffer sb = new StringBuffer();
		if(queryFields!=null){
			sb.append(queryFields).append(" ");
		}
		sb.append(fromJoinSubClause);	
		sb.append(" ").append(generateHqlWhereAppendBr(whereBodies, params));//此行有变动
		sb.append(" ").append(
				generateHqlOrderClause(orderField, orderDirection));
		String finalHql = sb.toString();

		return finalHql;
	}
	
	/**
	 * 根据Hibernate排序Order对象数组，生成对应的完整HQL排序语句。
	 * 
	 * @param orders
	 *            Order数组
	 * @return 完整HQL排序语句
	 */
	static String generateHqlOrderClause(String[] orders) {

		if (null == orders)
			return "";

		boolean isFirst = true;
		StringBuffer stringBuffer = new StringBuffer();
		for (String order : orders) {
			if (order != null) {
				if (isFirst) {
					stringBuffer.append(" order by ");
					isFirst = false;
				} else {
					stringBuffer.append(", ");
				}
				stringBuffer.append(order.toString());
			}

		}
		return stringBuffer.toString();
	}

	/**
	 * 根据HQL单个排序条件及生成对应的HQL排序语句。
	 * 
	 * @param orderField
	 *            排序字段
	 * @param orderDirection
	 *            升/降序条件
	 * @return 如果搜索字段为null或空，则返回空字符串；正常返回格式形如" ORDER BY XX ASC"
	 */
	private static String generateHqlOrderClause(String orderField,
			String orderDirection) {
		if (orderField == null || orderField.trim().length() <= 0) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(" order by ");
		stringBuffer.append(orderField).append(" ");
		if (orderDirection != null) {
			stringBuffer.append(orderDirection);
		}

		return stringBuffer.toString();
	}

	/**
	 * 根据多个"AND"运算的参数化的HQL where条件语句，及其对应的参数值Map，生成完整的HQL WHERE语句。
	 * 
	 * @param whereBodies
	 *            查询过滤条件的数组，类似"userName=:userName"
	 * @param params
	 *            whereBodies中参数名称对应的条件值
	 * @return 完整的HQL WHERE语句（包含"where" HQL 关键字）
	 */
	private static String generateHqlWhereClause(final String[] whereBodies,
			final Map<String, ?> params) {
		//Map<String, Object> paramres=(Map<String, Object>) params; 
		StringBuffer sb = new StringBuffer();
		String andOp = " and ";
		if (whereBodies != null && whereBodies.length > 0) {
			
			for (String whereBody : whereBodies) {
				if(StringUtil.isNullOrEmpty(whereBody)){
					break;
				}
				String paramName = getWhereBodyParamName(whereBody);
				if (paramName != null) {
					if (params != null && params.containsKey(paramName)) {
						// parameterized condition, remove non set parameters.
						if (params.get(paramName) != null && params.get(paramName).toString().length()>0) {
							// if param is not null , append
							// "and propName=propValue"
							
							if(params.get(paramName).getClass().getSimpleName().equals("Long[]")){//数组 类型是long类型 
								Long[] results=(Long[]) params.get(paramName);
								if(results.length>1000){
								//int number=results.length%1000;
									int number=gernateNumber(results.length);//次数
									//Long[] result=new Long[1000];
									for(int i=1;i<=number;i++){
										
										if(i==1){//第一次
											sb.append(andOp).append("(").append(whereBody)
											.append(" ");
											//params.put(paramName, result);
										}else{//后面循环
											if(i==2){
												whereBody=whereBody.replace(":"+paramName, ":"+paramName+i);
											}else{
												int a=i-1;
												whereBody=whereBody.replace(":"+paramName+a, ":"+paramName+i);
											}
											
											sb.append(" or ").append(whereBody);
											
											//params.put(paramName+i, result);
										}
									}
									sb.append(")");
								}else{
									sb.append(andOp).append("(").append(whereBody)
									.append(")");
								}
							}else{
								sb.append(andOp).append("(").append(whereBody)
								.append(")");
							}
						} else {
							// if param is null , condition ignored.
						}
					}
				} else {
					// unparameterized condition, just append together.
					sb.append(andOp).append("(").append(whereBody).append(")");
				}
			}
			if (sb.length() > 0) {
				sb.replace(0, andOp.length(), " where ");
			}
		}
		return sb.toString();
	}
	
	
	
	/**
	 * 修改人：CMM
	 * Team模块用
	 * 注：此函数就是在where后加入了左小括号
	 * 
	 * 根据多个"AND"运算的参数化的HQL where条件语句，及其对应的参数值Map，生成完整的HQL WHERE语句。
	 * 
	 * @param whereBodies
	 *            查询过滤条件的数组，类似"userName=:userName"
	 * @param params
	 *            whereBodies中参数名称对应的条件值
	 * @return 完整的HQL WHERE语句（包含"where (" HQL 关键字）
	 */
	private static String generateHqlWhereAppendBr(final String[] whereBodies,
			final Map<String, ?> params) {
		StringBuffer sb = new StringBuffer();
		String andOp = " and ";
		if (whereBodies != null && whereBodies.length > 0) {
			for (String whereBody : whereBodies) {
				String paramName = getWhereBodyParamName(whereBody);
				if (paramName != null) {
					if (params != null && params.containsKey(paramName)) {
						// parameterized condition, remove non set parameters.
						if (params.get(paramName) != null && params.get(paramName).toString().length()>0) {
							// if param is not null , append
							// "and propName=propValue"
							sb.append(andOp).append("(").append(whereBody)
									.append(")");
						} else {
							// if param is null , condition ignored.
						}
					}
				} else {
					// unparameterized condition, just append together.
					sb.append(andOp).append("(").append(whereBody).append(")");
				}
			}
			if (sb.length() > 0) {
				sb.replace(0, andOp.length(), " where (");//此行有变动
			}
		}
		return sb.toString();
	}

	/**
	 * 返回HQL语句中需要使用的参数化Where条件语句中的参数名称。
	 * <p>
	 * 如：有条件语句param1=:param1Name作为参数传入，则返回该条件语句的参数名称param1Name
	 * <p>
	 * <b>注意：</b>参数只接受一条Where条件语句
	 * 
	 * @param original
	 *            HQL参数化Where条件语句
	 * @return Where条件语句的参数名称
	 */
	private static String getWhereBodyParamName(String original) {
		if (StringUtil.isNullOrEmpty(original)||!original.contains(":")) {
			return null;
		}
		String[] oris = original.split("[:()]");
		if (oris.length == 1) {
			return null;
		} else {
			return oris[oris.length - 1].trim();
		}
	}
	
	
	/**
	 * Oracle模糊查询符
	 */
	public static final String LIKE_CHAR = "%";
	
	/**
	 * 将输入的前后附加上模糊查询符，并且小写化，以支持忽略大小写模糊查询
	 * @param ori Object,一般应该是String
	 * @return
	 */
	public static String fullILike(Object ori){
		if(ori==null){
			return null;
		}else{
			return LIKE_CHAR + ori.toString().toLowerCase() + LIKE_CHAR;
		}
	}
	/**
	 * 获取绑定参数
	 * @param sql
	 * @param hql
	 * @return
	 */
	public static String getParametors(String hql,String sql){
		int begin=hql.indexOf("where")+6;
		int end=0;
		if(hql.contains("order")){//含有排序
			end=hql.indexOf("order");
		}
		if(end!=0){
			hql =hql.substring(begin, end);
		}else{
			hql=hql.substring(begin);
		}
		String[] conditions=hql.split("and");
		
		for(String condition:conditions){
			if(condition.contains("(")&&condition.contains(")") &&condition.indexOf("(")==0){
				condition=condition.substring(condition.indexOf("(")+1, condition.indexOf(")"));
				condition=condition.substring(condition.indexOf(":")+1, condition.length());
			}else{
				if(condition.contains(")")){
					condition=condition.substring(condition.indexOf(":")+1, condition.indexOf(")"));
				}else{
					condition=condition.substring(condition.indexOf(":")+1, condition.length());
				}
				
			}
			sql=sql.substring(0, sql.indexOf("?"))+":"+condition+sql.substring(sql.indexOf("?")+1);
		}
		
		return sql;
	}
	/**
	 * 计划次数
	 * @param total
	 */
	public static int gernateNumber(int total){
		if(total%1000!=0){
			return total/1000+1;
		}else{
			return total/1000;
		}
	}
}
