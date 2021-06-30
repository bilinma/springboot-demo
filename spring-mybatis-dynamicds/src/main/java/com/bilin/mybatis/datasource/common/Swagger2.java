package com.bilin.mybatis.datasource.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xiaobin.ma
 *
 */
@Configuration  
@EnableSwagger2  
public class Swagger2 {  
  
    @Value("${swagger.ui.enable:false}") //该配置项在配置中心管理  
    private boolean environmentSpecificBooleanFlag;
    @Value("${swagger.ui.basePackage:com.bilin.mybatis.datasource.web}")
    private String basePackage;
      
    @Bean  
    public Docket createRestApi() {// 创建API基本信息

        //添加head参数配置start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("SiteCode").description("站点编码").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())  
                .select()  
                .apis(RequestHandlerSelectors.basePackage(basePackage))// 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外  
                .paths(PathSelectors.any())  
                .build().enable(environmentSpecificBooleanFlag)
                .globalOperationParameters(pars);
    }  
  
    private ApiInfo apiInfo() {// 创建API的基本信息，这些信息会在Swagger UI中进行显示  
        return new ApiInfoBuilder()  
                .title("Spring Cloud中使用Swagger2构建RESTful APIs")// API 标题  
                .description("提供的RESTful APIs")// API描述  
                .contact("马小斌")// 联系人  
                .version("1.0")// 版本号  
                .build();  
    }  
    
}  