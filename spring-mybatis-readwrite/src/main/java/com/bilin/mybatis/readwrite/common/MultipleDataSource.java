package com.bilin.mybatis.readwrite.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @author 马小斌
 * @date 2019年7月1日
 *
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return RouteContextHolder.getDataSource();
    }
}
