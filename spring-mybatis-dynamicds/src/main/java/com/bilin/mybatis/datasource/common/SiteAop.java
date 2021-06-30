/*
package com.bilin.mybatis.datasource.common;

import com.chinasofti.hwpay.common.RouteContextHolder;
import com.chinasofti.hwpay.common.constant.DataSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

*/
/**
 * @author : championjing
 * @ClassName: ChinaSiteAop
 * @Description:
 * @Date: 7/5/2019 3:43 PM
 *//*

@Configuration
@Aspect
@Slf4j
public class SiteAop {
    
    @Pointcut("execution(public void com.chinasofti.hwpay.job.china.*.*(..) )")
    public void chinaPoint(){ }

    @Pointcut("execution(public void com.chinasofti.hwpay.job.inter.*.*(..) )")
    public void interPoint(){ }
    
    @Before("chinaPoint() && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void setChina(JoinPoint jp){
        Signature signature = jp.getSignature();
        log.info("自动添加站点信息为定时任务:{}",signature.getName());  
        RouteContextHolder.setDataSource(DataSourceEnum.BROKER_CHINA.getValue());
    }
    
    @After("chinaPoint() && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void removeChinaSite(JoinPoint jp){
        Signature signature = jp.getSignature();
        RouteContextHolder.clearDataSource();
        log.info("移除站点信息:{}",signature.getName());
    }
    @AfterThrowing("chinaPoint() && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void chinaHandleException(JoinPoint jp){
        Signature signature = jp.getSignature();
        log.error("定时任务执行出现异常:{}",signature.getName());
        RouteContextHolder.clearDataSource();
        log.info("移除站点信息");
    }

    @Before("interPoint() && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void setInter(JoinPoint jp){
        Signature signature = jp.getSignature();

        log.info("自动添加站点信息为定时任务:{}",signature.getName());
        RouteContextHolder.setDataSource(DataSourceEnum.BROKER_INTER.getValue());
    }

    @After("interPoint() && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void removeInterSite(JoinPoint jp){
        Signature signature = jp.getSignature();
        RouteContextHolder.clearDataSource();
        log.info("移除站点信息:{}",signature.getName());
    }
    @AfterThrowing("interPoint() && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void interHandleException(JoinPoint jp){
        Signature signature = jp.getSignature();
        log.error("定时任务执行出现异常:{}",signature.getName());
        RouteContextHolder.clearDataSource();
        log.info("移除站点信息");
    }
}
*/
