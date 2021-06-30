package com.bilin.mybatis.datasource.web;

import com.bilin.mybatis.datasource.entity.User;
import com.bilin.mybatis.datasource.service.IUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

	private static Log logger = LogFactory.getLog(UserController.class);


	@Autowired
	private IUserService userService;
	

	@RequestMapping(value = "/hello/{myName}", method = RequestMethod.GET)
	@ResponseBody
	public String hello(@PathVariable String myName) {
		logger.debug("apache log:" + myName);
		return "Hello " + myName + "!!!";
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getUserById(@PathVariable Long id) {
		logger.debug("apache log:" + id);
		User user = userService.selectByPrimaryKey(id);
		return user.getName();
	}


	@RequestMapping(value = "/insert", method = RequestMethod.PUT)
	@ResponseBody
	public void getUserById(User user) {
		logger.debug("apache log:" + user);
		userService.instert(user);
	}



}
