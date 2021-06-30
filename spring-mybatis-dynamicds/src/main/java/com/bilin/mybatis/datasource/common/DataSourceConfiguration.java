package com.bilin.mybatis.datasource.common;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.bilin.mybatis.datasource.common.constant.DataSourceEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

	@Bean(name = "brokerChina")
	@ConfigurationProperties(prefix = "spring.datasource.druid.brokerchina")
	public DataSource brokerChina() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "brokerInter")
	@ConfigurationProperties(prefix = "spring.datasource.druid.brokerinter")
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
	public DataSource multipleDataSource(@Qualifier("brokerChina") DataSource brokerChina, @Qualifier("brokerInter") DataSource brokerInter) {
		MultipleDataSource multipleDataSource = new MultipleDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceEnum.BROKER_CHINA.getValue(), brokerChina);
		targetDataSources.put(DataSourceEnum.BROKER_INTER.getValue(), brokerInter);
		// 添加数据源
		multipleDataSource.setTargetDataSources(targetDataSources);
		// 设置默认数据源
		multipleDataSource.setDefaultTargetDataSource(brokerChina());
		return multipleDataSource;
	}
	
	
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(multipleDataSource(brokerChina(),brokerInter()));
        //此处设置为了解决找不到mapper文件的问题
        //sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.bilin.mybatis.datasource.entity");
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
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
