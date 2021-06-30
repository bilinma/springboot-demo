package com.bilin.mybatis.datasource.service;

import com.bilin.mybatis.datasource.entity.User;

import java.util.concurrent.Future;


public interface IUserService {

	public User selectByPrimaryKey(Long id);
	
	public User getUserByIdCache1(Long id);
	
	public User getUserByIdCache2(Long id);
	
	public void getUserByIdCache3(Long id);
	
	public Future<User> getUser(Long id);

	public void instert(User user);
	
}
