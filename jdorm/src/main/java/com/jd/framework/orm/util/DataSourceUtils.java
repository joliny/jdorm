package com.jd.framework.orm.util;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jd.framework.orm.common.PropertiesFile;
import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.exception.SystemException;

/**
 * 负责全局 DataSource 的获取
 */
public abstract class DataSourceUtils {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtils.class);
    private static final String JDBC_PROPERTIES_FILE = "/jdbc.properties";
    private static final DataSource dataSource = doGetDataSource();
    private static final Dialect dialect = JdbcUtils.doGetDialect(dataSource);

    public static DataSource getDataSource( ) {
        return dataSource;
    }

    public static void closeDataSource() {
        try {
        	Method method=dataSource.getClass().getMethod("close");
        	//FastClass.create(dataSource.getClass()).getMethod(method).invoke(dataSource,null);
        	method.invoke(dataSource);
        } catch (NoSuchMethodException e) {
        	log.error("Unabled to found NoSuchMethodException!!! ", e);
        } catch (Exception e) {
            log.error("Unabled to destroy DataSource!!! ", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static DataSource doGetDataSource() {
        try {
        	//System.out.println(DataSourceUtils.class.getResource(""));
        	InputStream file = DataSourceUtils.class.getResourceAsStream(JDBC_PROPERTIES_FILE);
            PropertiesFile config = new PropertiesFile(file);
            Class<DataSource> dataSourceClass = (Class<DataSource>) config.asClass("jdbc.dataSource");
            if (dataSourceClass == null) {
                throw new SystemException("jdbc.dataSource == null.");
            }
            log.info("Using DataSource : " + dataSourceClass.getName());

            DataSource dataSource = dataSourceClass.newInstance();

            Properties properties = config.sub("jdbc.").getProperties();
            properties.remove("datasource");

            for (Entry<?, ?> entry : properties.entrySet()) {
                String name = (String) entry.getKey();
                Object value = entry.getValue();
                BeanUtils.setProperty(dataSource, name, value);
            }

            if (dataSource.getClass().getName().indexOf("c3p0") > 0) {
                //Disable JMX in C3P0
                System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator", "com.mchange.v2.c3p0.management.NullManagementCoordinator");
            }

            try {
            	Method method=dataSource.getClass().getMethod("init");
            	method.invoke(dataSource);
            	//FastClass.create(dataSource.getClass()).getMethod(method).invoke(dataSource,null);
            } catch (NoSuchMethodException e) {
            	log.error("Unabled to found NoSuchMethodException!!! ", e);
            } catch (Exception e) {
                log.error("Unabled to init DataSource!!! ", e);
            }

            return dataSource;

        } catch (Exception e) {
            throw SystemException.unchecked(e);
        }
    }

    public static Dialect getDialect() {
        return dialect;
    }
    public static void main(String[] args) {
		doGetDataSource();
	}
}
