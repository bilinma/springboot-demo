package com.bilin.mybatis.datasource.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * [简要描述]:WebMvcConfigurationSupport代替 WebMvcConfigurerAdapter 可以进行更多类型的定制。
 * <br/>
 * [详细描述]:<br/>
 * 
 * @version 1.0 *
 * @since
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter  {

	@Resource
	private DbRouteInfoInterceptor dbRouteInfoInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(dbRouteInfoInterceptor).addPathPatterns("/**")
			.excludePathPatterns("/v2/**", "/configuration/**", "/swagger-resources/**","/swagger-ui.html", "/webjars/**","/static/**");
		
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//文件名避免使用汉字
		registry.addResourceHandler("/static/**.xlsx","/static/**.doc").addResourceLocations("classpath:/static/");
		super.addResourceHandlers(registry);
	}



}
