package com.jd.framework.orm.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 
*    
* 项目名称：petroleum   
* 类名称：PageResult   
* 类描述：分页查询工具   
* 创建人：liubing   
* 创建时间：2012-6-7 下午10:02:12   
* 修改人：liubing   
* 修改时间：2012-6-7 下午10:02:12   
* 修改备注：   
* @version    
*
 */
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = 9142640042556747848L;

	/**
	 * 当前记录索引
	 */
	private int currentIndex = 0;

	/**
	 * 总记录数
	 */
	private Long totalCount = 0L;

	/**
	 * 当前页面
	 */
	private int currentPage = 1;

	/**
	 * 总记录的页面数
	 */
	private int totalPage = 0;

	/**
	 * 每页显示的记录数，默认为10条记录 如果要显示全部记录数，可以设定每页记录数为 0
	 */
	private int pageSize = 1;

	/**
	 * 返回的数据库记录列表
	 */
	private List<T> content = new ArrayList<T>(0);

	/**
	 * 查询表达式
	 */
	private String queryString;

	/**
	 * 查询时间
	 */
	private long queryTime;

	/**
	 * @return the currentIndex
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * @param currentIndex
	 *            the currentIndex to set
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * @return the totalCount
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the countPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param countPage
	 *            the countPage to set
	 */
	public void setTotalPage(int countPage) {
		this.totalPage = countPage;
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
	 * @return the content
	 */
	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		if (content == null)
			this.content = new ArrayList<T>(0);
		else
			this.content = content;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString
	 *            the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the queryTime
	 */
	public long getQueryTime() {
		return queryTime;
	}

	/**
	 * @param queryTime
	 *            the queryTime to set
	 */
	public void setQueryTime(long queryTime) {
		this.queryTime = queryTime;
	}
	

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n").append("QueryString: ")
				.append(this.getQueryString());
		buffer.append("\n").append("QueryTime: ").append(this.getQueryTime());
		buffer.append("\n").append("CurrentIndex: ")
				.append(this.getCurrentIndex());
		buffer.append("\n").append("TotalCount: ").append(this.getTotalCount());
		buffer.append("\n").append("CurrentPage: ")
				.append(this.getCurrentPage());
		buffer.append("\n").append("TotalPage: ").append(this.getTotalPage());
		buffer.append("\n").append("PageSize: ").append(this.getPageSize());
		buffer.append("\n");
		for (int i = 0; i < this.getContent().size(); i++) {
			buffer.append("\n.[" + i + "]").append(this.getContent().get(i));
		}
		return buffer.toString();
	}
	
}
