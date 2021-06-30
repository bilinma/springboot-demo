package com.bilin.mybatis.datasource;

import com.bilin.mybatis.datasource.dao.BaseMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages={"com.bilin.mybatis.datasource.dao"}, markerInterface= BaseMapper.class)
public class SpringMybatisDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisDatasourceApplication.class, args);
    }

}
