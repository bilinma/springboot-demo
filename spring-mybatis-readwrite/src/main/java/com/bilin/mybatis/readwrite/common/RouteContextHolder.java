package com.bilin.mybatis.readwrite.common;

import org.springframework.stereotype.Component;


/**
 * 
 * @author 马小斌
 * @date 2019年7月1日
 *
 */
@Component
public class RouteContextHolder {

    private static final ThreadLocal<String> dbContextHolder = new InheritableThreadLocal<>();
    
	public static void initRouteContext() {
		dbContextHolder.set(null);
	}
	
    /**
     *  设置数据源
     * @param db
     */
    public static void setDataSource(String db){
    	dbContextHolder.set(db);
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDataSource(){
        return dbContextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDataSource(){
    	dbContextHolder.remove();
    }
    

}
