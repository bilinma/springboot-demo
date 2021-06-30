package com.bilin.mybatis.datasource.common.constant;

/**
 * 
 * @author 马小斌
 * @date 2019年7月1日
 *
 */
public enum DataSourceEnum {

	BROKER_CHINA("broker_china"),
	BROKER_INTER("broker_inter");

    private String value;

    DataSourceEnum(String value){
    	this.value=value;
    }

    public String getValue() {
        return value;
    }
}
