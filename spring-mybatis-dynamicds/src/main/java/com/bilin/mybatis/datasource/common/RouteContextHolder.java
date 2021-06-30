package com.bilin.mybatis.datasource.common;

import com.bilin.mybatis.datasource.common.constant.DataSourceEnum;
import com.bilin.mybatis.datasource.common.constant.SiteInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



/**
 * 
 * @author 马小斌
 * @date 2019年7月1日
 *
 */
@Component
public class RouteContextHolder {

    private static final ThreadLocal<String> dbContextHolder = new InheritableThreadLocal<>();
    
    private static final ThreadLocal<String> siteContextHolder = new InheritableThreadLocal<>();
    
    public static final Map<String, String> ROUTE_2_DATASOURCE_MAP = new HashMap<String, String>();
	static {
		ROUTE_2_DATASOURCE_MAP.put(SiteInfo.China.code,DataSourceEnum.BROKER_CHINA.getValue());
		ROUTE_2_DATASOURCE_MAP.put(SiteInfo.Inter.code,DataSourceEnum.BROKER_INTER.getValue());
	}

	public static void initRouteContext() {
		dbContextHolder.set(null);
		siteContextHolder.set(null);
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
    
    
    
    /**
     *  设置站点
     * @param db
     */
    public static void setSiteSource(String siteCode){
    	siteContextHolder.set(siteCode);
    }

    /**
     * 取得当前站点
     * @return
     */
    public static String getSiteSource(){
        return siteContextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearSiteSource(){
    	siteContextHolder.remove();
    }
    
    public static String getRoute2Datasource(String siteCode) {
		return ROUTE_2_DATASOURCE_MAP.get(siteCode);
	}
}
