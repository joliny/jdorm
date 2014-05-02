/**   
* @Title: TestDemoConnectDaoImpl.java 
* @Package test.jd.framework.orm.dao.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午8:14:31 
* @version V1.0   
*/ 
package test.jd.framework.orm.dao.impl;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.jd.framework.orm.entity.DemoEntity;

import com.jd.framework.orm.complex.transaction.ResourceManager;
import com.jd.framework.orm.complex.transaction.TransactionDefinition;
import com.jd.framework.orm.complex.transaction.TransactionManager;
import com.jd.framework.orm.complex.transaction.TransactionStatus;
import com.jd.framework.orm.complex.transaction.support.jdbc.JdbcResourceManager;
import com.jd.framework.orm.complex.transaction.support.jdbc.JdbcSession;

import junit.framework.TestCase;

/**
 * @author liubing1@jd.com
 *
 */
public class TestDemoConnectDaoImpl extends TestCase {
	
	public void test() throws Exception{
		TransactionManager manager = new TransactionManager();
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoEntity  demoEntity =new DemoEntity();
		DataSource dataSource=(DataSource) context.getBean(demoEntity.getSchemaInfo().getReadDataBase());
		ResourceManager<JdbcSession> rm = new JdbcResourceManager(dataSource);
		manager.registerResourceManager(rm);
		TransactionStatus status = manager.requestTransaction(new TransactionDefinition().propagationRequired());
		JdbcSession session = status.requestResource(JdbcSession.class);
		Connection connection = session.getConnection();
		DemoConnectDaoImpl connectDaoImpl=(DemoConnectDaoImpl) context.getBean("DemoConnectDaoImpl");
		
		demoEntity=connectDaoImpl.findById(connection,90);
		status.commit();
		manager.close();
		System.out.println(connection.isClosed());
	}
	
	public void test2() throws Exception{
		TransactionManager manager = new TransactionManager();
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoEntity  demoEntity =new DemoEntity();
		DataSource dataSource=(DataSource) context.getBean(demoEntity.getSchemaInfo().getReadDataBase());
		ResourceManager<JdbcSession> rm = new JdbcResourceManager(dataSource);
		manager.registerResourceManager(rm);
		TransactionStatus status = manager.requestTransaction(new TransactionDefinition().propagationRequired());
		JdbcSession session = status.requestResource(JdbcSession.class);
		Connection connection = session.getConnection();
		DemoConnectDaoImpl connectDaoImpl=(DemoConnectDaoImpl) context.getBean("DemoConnectDaoImpl");
		demoEntity.setId(demoEntity.generateId(demoEntity.getSchemaInfo().getWriteDataBase(), demoEntity.getSchemaInfo().getTableName()));
		demoEntity.setName("333");
		connectDaoImpl.doCreateEntity(connection, demoEntity);
		//Integer.parseInt("ss");
		status.commit();
		manager.close();
	}
}
