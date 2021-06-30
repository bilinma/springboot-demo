package com.bilin.mybatis.datasource.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 数据路由及站点信息拦截器
 * @author 马小斌
 * @date 2019年7月10日
 *
 */
@Component
public class DbRouteInfoInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(DbRouteInfoInterceptor.class.getName());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handlerMethod)
			throws Exception {
		try {
			StringBuffer url = request.getRequestURL();
			// 获取Head中SiteCode 站点信息
			String siteCode = getHeaderValue(request, "SiteCode");
			logger.info("sessionId:{},siteCode：{}, method:{}, url :{}", request.getSession().getId(), siteCode,request.getMethod(), url);
			RouteContextHolder.setSiteSource(siteCode);
			RouteContextHolder.setDataSource(RouteContextHolder.getRoute2Datasource(siteCode));

		} catch (Exception e) {
			logger.error("current thread " + Thread.currentThread().getName() + " add data to ThreadLocal error", e);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		StringBuffer url = request.getRequestURL();
		logger.info("移除siteCode:{}, method:{}, url :{}", RouteContextHolder.getSiteSource(),request.getMethod(), url);
		RouteContextHolder.clearDataSource();
		RouteContextHolder.clearSiteSource();
	}

	public static String getHeaderValue(HttpServletRequest req, String key) {
		String headValue = req.getParameter(key);
		if (StringUtils.isBlank(headValue)) {
			headValue = req.getHeader(key);
		}
		if (StringUtils.isBlank(headValue)) {
			Cookie[] cookies = req.getCookies();
			if ((cookies != null) && (cookies.length > 0)) {
				for (Cookie c : cookies)
					if (key.equals(c.getName()))
						headValue = c.getValue();
			}
		}
		return headValue;
	}
}
