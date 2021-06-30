package com.bilin.mybatis.readwrite.common;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.bilin.mybatis.readwrite.common.constant.DataSourceEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author 马小斌
 * @Date 2019年7月1日
 *
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

	@Bean(name = "writeDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.write-datasource")
	public DataSource brokerChina() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "readDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.read-datasource")
	public DataSource brokerInter() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 动态数据源配置
	 * 
	 * @return
	 */
	@Bean
	@Primary
	public DataSource multipleDataSource(@Qualifier("writeDataSource") DataSource writeDataSource, @Qualifier("readDataSource") DataSource readDataSource) {
		MultipleDataSource multipleDataSource = new MultipleDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceEnum.WRITE_DATASOURCE.getValue(), writeDataSource);
		targetDataSources.put(DataSourceEnum.READ_DATASOURCE.getValue(), readDataSource);
		// 添加数据源
		multipleDataSource.setTargetDataSources(targetDataSources);
		// 设置默认数据源
		multipleDataSource.setDefaultTargetDataSource(brokerChina());
		return multipleDataSource;
	}
	
	
    @Bean
	@Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(multipleDataSource(brokerChina(),brokerInter()));
        //此处设置为了解决找不到mapper文件的问题
        //sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.bilin.mybatis.datasource.entity");
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:META-INF/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /**
     * 事务管理
     *
     * @return 事务管理实例
     */
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(multipleDataSource(brokerChina(),brokerInter()));
    }

}
