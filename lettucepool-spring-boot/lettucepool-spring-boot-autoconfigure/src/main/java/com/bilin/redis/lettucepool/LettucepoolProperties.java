package com.bilin.redis.lettucepool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.bilin.jedispool")
public class LettucepoolProperties {

    @Value("${spring.redis.cache.nodes:127.0.0.1:6379}")
    private String nodes;
    @Value("${spring.redis.cache.password:}")
    private String password;
    @Value("${spring.redis.cache.maxIdle:8}")
    private Integer maxIdle;
    @Value("${spring.redis.cache.minIdle:1}")
    private Integer minIdle;
    @Value("${spring.redis.cache.maxTotal:8}")
    private Integer maxTotal;
    @Value("${spring.redis.cache.maxWaitMillis:5000}")
    private Long maxWaitMillis;

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

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }
}
