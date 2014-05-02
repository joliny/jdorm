package test.jd.framework.orm.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import junit.framework.TestCase;
import com.jd.framework.orm.complex.transaction.ResourceManager;
import com.jd.framework.orm.complex.transaction.TransactionDefinition;
import com.jd.framework.orm.complex.transaction.TransactionManager;
import com.jd.framework.orm.complex.transaction.TransactionStatus;
import com.jd.framework.orm.complex.transaction.support.jdbc.JdbcResourceManager;
import com.jd.framework.orm.complex.transaction.support.jdbc.JdbcSession;
import com.jd.framework.orm.jdbc.PreparedStatementCreator;
public class TestCommonTransaction extends TestCase{
	
	public void test() throws Exception{
		TransactionManager manager = new TransactionManager();
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DataSource dataSource=(DataSource) context.getBean("dataSource");
		ResourceManager<JdbcSession> rm = new JdbcResourceManager(dataSource);
		manager.registerResourceManager(rm);
		TransactionStatus status = manager.requestTransaction(new TransactionDefinition().propagationRequired());
		JdbcSession session = status.requestResource(JdbcSession.class);
		Connection connection = session.getConnection();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("name", "aad");
		params.put("id", 6);
		PreparedStatement ps = PreparedStatementCreator.createPreparedStatement(connection, "insert into demo (id,name) values (:id,:name)", params);
        ps.executeUpdate();
           // params.put("id", 6);
        ps = PreparedStatementCreator.createPreparedStatement(connection, "insert into test2 (id) values (?)", 6);
        ps.executeUpdate();
            
            
        status.commit();
		manager.close();
	}
	
	public void test2() throws Exception{
		TransactionManager manager = new TransactionManager();
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DataSource dataSource=(DataSource) context.getBean("dataSource");
			ResourceManager<JdbcSession> rm = new JdbcResourceManager(dataSource);
		    manager.registerResourceManager(rm);
		    TransactionStatus status = manager.requestTransaction(new TransactionDefinition().propagationRequired());
		    JdbcSession session = status.requestResource(JdbcSession.class);
		    Connection connection = session.getConnection();
		    List<Object> results=new ArrayList<Object>();
		    results.add(8);
		    results.add("aads");
		    PreparedStatement ps = PreparedStatementCreator.createPreparedStatement(connection, "insert into demo (id,name) values (?,?)", results);
            ps.executeUpdate();
            status.commit();
		    manager.close();
	}
	
	public void test3() throws Exception{
		TransactionManager manager = new TransactionManager();
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DataSource dataSource=(DataSource) context.getBean("dataSource");
			ResourceManager<JdbcSession> rm = new JdbcResourceManager(dataSource);
		    manager.registerResourceManager(rm);
		    TransactionStatus status = manager.requestTransaction(new TransactionDefinition().propagationRequired());
		    JdbcSession session = status.requestResource(JdbcSession.class);
		    Connection connection = session.getConnection();
		    Object[] results=new Object[2];
		    results[0]=9;
		    results[1]="aads";
		    PreparedStatement ps = PreparedStatementCreator.createPreparedStatement(connection, "insert into demo (id,name) values (?,?)", results);
            ps.executeUpdate();
            status.commit();
		    manager.close();
	}
}
