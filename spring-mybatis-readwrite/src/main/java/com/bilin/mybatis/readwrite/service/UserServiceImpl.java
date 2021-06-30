package com.bilin.mybatis.readwrite.service;

import com.alibaba.fastjson.JSONObject;
import com.bilin.mybatis.readwrite.dao.IUserMapper;
import com.bilin.mybatis.readwrite.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private IUserMapper userDao;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;


	@Cacheable(value = "common", key = "'id_'+#id")
	public User selectByPrimaryKey(Long id) {
		System.out.println("======================");
		return userDao.selectByPrimaryKey(id);
	}

	/**
	 * Mybatis 一级缓存
	 * 操作：开启一级缓存，范围为会话级别，调用三次
	 * 结论：只有第一次真正查询了数据库，后续的查询使用了一级缓存
	 */
	public User getUserByIdCache1(Long id) {
		System.out.println("======================");
		User user = userDao.selectByPrimaryKey(id);
		user = userDao.selectByPrimaryKey(id);
		user = userDao.selectByPrimaryKey(id);
		return user;
	}
	
	/**
	 * Mybatis 一级缓存
	 * 操作：增加了对数据库的修改操作，验证在一次数据库会话中，如果对数据库发生了修改操作，一级缓存是否会失效。
	 * 结论：在修改操作后执行的相同查询，查询了数据库，一级缓存失效
	 */
	public User getUserByIdCache2(Long id) {
		System.out.println("======================");
		userDao.selectByPrimaryKey(id);
		User user = new User();
		user.setName("小明");
		userDao.insert(user);
		userDao.selectByPrimaryKey(id);
		return user;
	}

	/**
	 * Mybatis 一级缓存
	 * 操作：开启两个SqlSession，在sqlSession1中查询数据，使一级缓存生效，在sqlSession2中更新数据库，验证一级缓存只在数据库会话内部共享
	 * 结论：出现了脏数据，也证明了之前的设想，一级缓存只在数据库会话内部共享。
	 */
	public void getUserByIdCache3(Long id) {
		System.out.println("======================");
		SqlSession sqlSession1 = sqlSessionFactory.openSession(true); 
        SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
        IUserMapper userMapper1 = sqlSession1.getMapper(IUserMapper.class);
        IUserMapper userMapper2 = sqlSession2.getMapper(IUserMapper.class);
        System.out.println("userMapper1读取数据: " + userMapper1.selectByPrimaryKey(id));
        System.out.println("userMapper1读取数据: " + userMapper1.selectByPrimaryKey(id));
        System.out.println("userMapper2读取数据: " + userMapper2.selectByPrimaryKey(id));
        System.out.println("userMapper2读取数据: " + userMapper2.selectByPrimaryKey(id));
        
		User user = new User();
		user.setId(1L);
		user.setName("小岑");
        
        System.out.println("userMapper2更新了" + userMapper2.updateByPrimaryKey(user) + "个用户的数据");
        System.out.println("userMapper1读取数据: " + userMapper1.selectByPrimaryKey(id));
        System.out.println("userMapper2读取数据: " + userMapper2.selectByPrimaryKey(id));
        
	}
	
	
	@Override
	@Async
	public Future<User> getUser(Long id) {
		try {  
            Thread.sleep(2000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }
		User user  = userDao.selectByPrimaryKey(id);
		System.out.println("f1 : " + Thread.currentThread().getName() + "  User=" + JSONObject.toJSONString(user));
		return new AsyncResult<>(user);
	}


	@Override
	public void instert(User user) {
		userDao.insert(user);
	}
	
	
	
}
