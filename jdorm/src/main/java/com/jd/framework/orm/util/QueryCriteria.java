package com.jd.framework.orm.util;
import java.util.HashMap;
import java.util.Map;
/**
 * 
*    
* 项目名称：petroleum   
* 类名称：QueryCriteria   
* 类描述：分页查询辅助类   
* 创建人：liubing   
* 创建时间：2012-6-7 下午10:02:58   
* 修改人：liubing   
* 修改时间：2012-6-7 下午10:02:58   
* 修改备注：   
* @version    
*
 */
public class QueryCriteria implements java.io.Serializable {

	private static final long serialVersionUID = 8462688883033832243L;

	public static final String SORT_DIRECTION_ASC = "ASC";

	public static final String SORT_DIRECTION_DESC = "DESC";
	
	private Object object;
	
	private boolean iscustom;
	
	private String sql;
	/**
	 * The start index of results to get, starts with 0.
	 */
	private int startIndex = 0;

	/**
	 * max query result line number.
	 */
	private int pageSize = 10;

	/**
	 * Order field, null if order not required.
	 */
	private String orderField = null;

	/**
	 * Order direction.
	 */
	private String orderDirection = SORT_DIRECTION_ASC;

	/**
	 * Query condition by specific business object in a pair of Key and Value.
	 */
	private Map<String, Object> queryCondition = new HashMap<String, Object>();

	public void addQueryCondition(String key, Object value) {
		this.queryCondition.put(key, value);
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the queryCondition
	 */
	public Map<String, Object> getQueryCondition() {
		return queryCondition;
	}

	/**
	 * @param queryCondition
	 *            the queryCondition to set
	 */
	public void setQueryCondition(Map<String, Object> queryCondition) {
		this.queryCondition = queryCondition;
	}

	/**
	 * @return the orderField
	 */
	public String getOrderField() {
		return orderField;
	}

	/**
	 * @param orderField
	 *            the orderField to set
	 */
	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}
	
	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the orderDirection
	 */
	public String getOrderDirection() {
		return orderDirection;
	}

	/**
	 * @param orderDirection
	 *            the orderDirection to set
	 */
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public void reset() {
		startIndex = 0;
		orderField = null;
		queryCondition.clear();

	}

	public boolean getIscustom() {
		return iscustom;
	}

	public void setIscustom(boolean iscustom) {
		this.iscustom = iscustom;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
