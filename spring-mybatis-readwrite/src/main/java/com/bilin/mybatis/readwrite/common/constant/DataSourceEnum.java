package com.bilin.mybatis.readwrite.common.constant;

/**
 * 
 * @author 马小斌
 * @date 2019年7月1日
 *
 */
public enum DataSourceEnum {

    WRITE_DATASOURCE("writeDataSource"),
    READ_DATASOURCE("readDataSource");

    private String value;

    DataSourceEnum(String value){
    	this.value=value;
    }

    public String getValue() {
        return value;
    }
}
