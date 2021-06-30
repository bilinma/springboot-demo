package com.bilin.mybatis.datasource.common.constant;

/**
 * 
 * @author 马小斌
 * @date 2019年7月1日
 *
 */
public enum SiteInfo {

	China("cn","中国站"),
	Inter("en","国际站");
	
	public String code;
	public String desc;
	
	private SiteInfo(String code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	
}
