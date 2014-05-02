
package test.jd.framework.orm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.jd.framework.orm.complex.transaction.ResourceManager;
import com.jd.framework.orm.complex.transaction.TransactionDefinition;
import com.jd.framework.orm.complex.transaction.TransactionManager;
import com.jd.framework.orm.complex.transaction.TransactionStatus;
import com.jd.framework.orm.complex.transaction.support.jdbc.JdbcResourceManager;
import com.jd.framework.orm.complex.transaction.support.jdbc.JdbcSession;
import com.jd.framework.orm.jdbc.PreparedStatementCreator;
import com.jd.framework.orm.transaction.Transaction;
import com.jd.framework.orm.util.PageResult;
import com.jd.framework.orm.util.QueryCriteria;
import test.jd.framework.orm.entity.DemoEntity;
/**
 * @author liubing1@jd.com
 *
 */
public class TestDemoDaoImpl extends TestCase{
	public void test1() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		demoEntity=daoImpl.findById(demoEntity.getSchemaInfo().getReadDataBase(),9);	
	}
	
	public void test2() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getReadDataBase();
		Transaction transaction=daoImpl.transaction(datasourname);
		daoImpl.findById(datasourname,9);
		transaction.commit();
	}
	
	public void test3() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getReadDataBase();
		Transaction  transaction=daoImpl.transaction(datasourname);
		List<DemoEntity> demoEntities=daoImpl.findAll(datasourname);
		System.out.println(demoEntities.size());
		transaction.commit();
		
	}
	
	public void test4() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		Transaction  transaction=daoImpl.transaction(datasourname);
		demoEntity.setId(demoEntity.generateId(demoEntity.getSchemaInfo().getWriteDataBase(),"demo"));
		demoEntity.setName("25");
		daoImpl.doCreateEntity(datasourname,demoEntity);
		transaction.commit();
	}
	
	public void test5()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="insert into demo (id,name) values (:id,:name)";
		for(int i=0;i<1000000;i++){
			DemoEntity params=new DemoEntity();
			params.setId(i);
			params.setName("qwer");
			daoImpl.doCreateBySql(datasourname,sql, params);
		}
		
		transaction.commit();
	}
	
	public void test6()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();

		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="insert into demo (id,name) values (:id,:name)";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id", 14);
		params.put("name", 14);
		daoImpl.doCreateBySql(datasourname,sql, params);
		transaction.commit();
	}
	
	public void test7()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="insert into demo (id,name) values (?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(15);
		params.add(15);
		daoImpl.doCreateBySql(datasourname,sql, params);
		transaction.commit();
	}
	
	public void test8()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="insert into demo (id,name) values (?,?)";
		//List<Object> params=new ArrayList<Object>();
		Object[] params=new Object[2];
		params[0]=16;
		params[1]=16;
		daoImpl.doCreateBySql(datasourname,sql, params);
		transaction.commit();
	}
	
	public void test9()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		Transaction  transaction=daoImpl.transaction(datasourname);
		DemoEntity params=new DemoEntity();
		params.setId(13);
		params.setName("qwer");
		daoImpl.doRemoveEntity(datasourname,params);
		transaction.commit();
	}
	
	public void test10() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="delete from  demo where id=:id";
		DemoEntity params=new DemoEntity();
		params.setId(12);
		daoImpl.doRemoveBySql(datasourname,sql, params);
		transaction.commit();
	}
	
	public void test11()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="delete from  demo where id=?";
		daoImpl.doRemoveBySql(datasourname,sql, 10);
		transaction.commit();
	}
	
	public void test12()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		//DemoEntity demoEntity=new DemoEntity();
		demoEntity.setId(2);
		demoEntity.setName("252");
		daoImpl.doUpdateEntity(datasourname,demoEntity);
		transaction.commit();
	}
	
	public void test13() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getWriteDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="update demo set name=:name where  id=:id";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id", 2);
		params.put("name", 14);
		daoImpl.doRemoveBySql(datasourname,sql, params);
		transaction.commit();
	}
	
	public void test14() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getReadDataBase();
		Transaction  transaction=daoImpl.transaction(datasourname);
		demoEntity.setId(2);
		List<DemoEntity> demoEntities=daoImpl.findByBean(datasourname,demoEntity);
		System.out.println(demoEntities.size());
		transaction.commit();
	}
	
	public void test15() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getReadDataBase();
		
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="select * from demo where id=?";
		demoEntity=(DemoEntity) daoImpl.findReturnObjectByParamsAndSql(datasourname,sql, DemoEntity.class, 2);
		System.out.println(demoEntity.getName());
		transaction.commit();
	}
	
	public void test16()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getReadDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		String sql="select count(*) as id from demo where id=?";
		int count=daoImpl.queryAsInt(datasourname,sql, 2);
		//DemoEntity demoEntity=(DemoEntity) daoImpl.findReturnObjectByParamsAndSql(sql, DemoEntity.class, 2);
		System.out.println(count);
		transaction.commit();
	}
	
	public void test17()throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");//此文件
		DemoDaoImpl daoImpl=(DemoDaoImpl) context.getBean("DemoDaoImpl");
		DemoEntity  demoEntity =new DemoEntity();
		String datasourname=demoEntity.getSchemaInfo().getReadDataBase();
		
		Transaction  transaction=daoImpl.transaction(datasourname);
		QueryCriteria criteria=new QueryCriteria();
		criteria.setOrderField("id");
		criteria.setOrderDirection("desc");
		criteria.setStartIndex(0);
		criteria.setPageSize(10);
		PageResult<DemoEntity> pageResult=daoImpl.findByCriteria(datasourname,criteria);
		System.out.println(pageResult.getTotalCount());
		transaction.commit();
	}
	
	public static void main(String[] args) {
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
            try {
    		    PreparedStatement ps = PreparedStatementCreator.createPreparedStatement(connection, "insert into demo (id,name) values (?,?)", results);
				ps.executeUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            status.commit();
		    manager.close();
	}
}
