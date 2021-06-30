package com.bilin.redis.jedispool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis 配置加载类
 *
 * @autor 马小斌
 * @date 2017年12月22日
 */
@Component
@ConfigurationProperties(prefix = "spring.bilin.jedispool")
public class JedispoolProperties {

    private String nodes = "127.0.0.1:6379";

    private String password;

    private Integer timeout = 5000;

    private Integer maxIdle = 300;

    private Integer maxTotal = 1000;

    private Integer maxWaitMillis = 1000;

    private Integer minEvictableIdleTimeMillis = 300000;

    private Integer numTestsPerEvictionRun = 1024;

    private long timeBetweenEvictionRunsMillis = 30000;

    private boolean testOnBorrow = true;

    private boolean testWhileIdle = true;

    private Integer maxRedirects = 3;

    private boolean isSentinel = false;

    private String senNodes = "127.0.0.1:26379";

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Integer maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Integer getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public boolean isSentinel() {
        return isSentinel;
    }

    public void setSentinel(boolean isSentinel) {
        this.isSentinel = isSentinel;
    }

    public String getSenNodes() {
        return senNodes;
    }

    public void setSenNodes(String senNodes) {
        this.senNodes = senNodes;
    }


}
