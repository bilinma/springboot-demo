package com.bilin.mybatis.readwrite;

import com.bilin.mybatis.readwrite.dao.BaseMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages={"com.bilin.mybatis.readwrite.dao"}, markerInterface= BaseMapper.class)
public class SpringMybatisReadwriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisReadwriteApplication.class, args);
    }

}
