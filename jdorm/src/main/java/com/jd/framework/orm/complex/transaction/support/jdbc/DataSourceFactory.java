/* Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.framework.orm.complex.transaction.support.jdbc;

import javax.sql.DataSource;

/**
 * dataSource工厂接口
 * @author yuqs
 * @since 0.1
 */
public interface DataSourceFactory {
	/**
	  * 获取dataSource
	  * @return DataSource
	  */
    DataSource getDataSource();
}
