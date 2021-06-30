package com.bilin.mybatis.readwrite.entity;

import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;

@Alias(value = "user")
@Entity
@Table(name = "t_user")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
