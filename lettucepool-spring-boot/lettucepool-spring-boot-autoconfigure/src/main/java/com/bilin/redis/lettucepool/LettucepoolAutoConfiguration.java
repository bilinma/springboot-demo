package com.bilin.redis.lettucepool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(LettucepoolProperties.class)
public class LettucepoolAutoConfiguration {

    private static Log logger = LogFactory.getLog(LettucepoolAutoConfiguration.class);

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(LettucepoolProperties lettucepoolProperties) {
        LettuceConnectionFactory connectionFactory = null;
        // 连接池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(lettucepoolProperties.getMaxIdle());
        poolConfig.setMinIdle(lettucepoolProperties.getMinIdle());
        poolConfig.setMaxTotal(lettucepoolProperties.getMaxTotal());
        poolConfig.setMaxWaitMillis(lettucepoolProperties.getMaxWaitMillis());
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        // 哨兵redis
        // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();

        String[] serverArray = lettucepoolProperties.getNodes().split("\\|");
        if(serverArray.length == 1){
            // 单机redis
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            if (!"".equals(serverArray)) {
                String[] ipAndPort = serverArray[0].split(":");
                String host = ipAndPort[0];
                String portStr = ipAndPort[1];
                int port = Integer.valueOf(portStr==null||"".equals(portStr)?"6379":portStr);
                redisConfig.setHostName(host);
                redisConfig.setPort(port);
            }
            if (null !=lettucepoolProperties.getPassword()) {
                redisConfig.setPassword(lettucepoolProperties.getPassword());
            }
            connectionFactory = new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        }else{
            // 集群redis
            RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
            Set<RedisNode> nodeses = new HashSet<>();
            for (String ipPort : serverArray) {
                String[] ipAndPort = ipPort.split(":");
                nodeses.add(new RedisNode(ipAndPort[0].trim(),Integer.valueOf(ipAndPort[1])));
            }
            redisConfig.setClusterNodes(nodeses);
            // 跨集群执行命令时要遵循的最大重定向数量
            redisConfig.setMaxRedirects(3);
            if (null !=lettucepoolProperties.getPassword()) {
                redisConfig.setPassword(lettucepoolProperties.getPassword());
            }
            connectionFactory = new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        }

        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }

    /**
     * @return
     */
    @Bean(name = "redisTemplateUtil")
    public RedisTemplateUtil getRedisTemplateUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisTemplateUtil(redisTemplate);
    }

}
