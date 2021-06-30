package com.bilin.redis.jedispool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(JedispoolProperties.class)
public class JedispoolAutoConfiguration {

    private static Log logger = LogFactory.getLog(JedispoolAutoConfiguration.class);

    /**
     * JedisPoolConfig 连接池
     *
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig(JedispoolProperties jedispoolProperties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(jedispoolProperties.getMaxIdle());
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(jedispoolProperties.getMaxTotal());
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(jedispoolProperties.getMaxWaitMillis());
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(jedispoolProperties.getMinEvictableIdleTimeMillis());
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(jedispoolProperties.getNumTestsPerEvictionRun());
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(jedispoolProperties.getTimeBetweenEvictionRunsMillis());
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(jedispoolProperties.isTestOnBorrow());
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(jedispoolProperties.isTestWhileIdle());

        return jedisPoolConfig;
    }

    /**
     * 配置工厂
     *
     * @param @param  jedisPoolConfig
     * @param @return
     * @return JedisConnectionFactory
     * @throws
     * @Title: JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig, JedispoolProperties jedispoolProperties) {
        JedisConnectionFactory jedisConnectionFactory = null;
        //Set<RedisNode> clusterNodes
        String[] serverArray = jedispoolProperties.getNodes().split("\\|");
        logger.debug(jedispoolProperties.getNodes());
        if (jedispoolProperties.isSentinel()) {
            //SpringBoot整合Redis的哨兵模式
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
            //配置matser的名称
            String[] ipAndPort = serverArray[0].split(":");
            RedisNode redisNode = new RedisNode(ipAndPort[0], Integer.valueOf(ipAndPort[1]));
            redisNode.setName("mymaster");
            sentinelConfig.master(redisNode);
            //配置redis的哨兵sentinel
            Set<RedisNode> redisNodeSet = new HashSet<>();
            for (String ipPort : jedispoolProperties.getSenNodes().split("\\|")) {
                String[] senIpAndPort = ipPort.split(":");
                RedisNode senRedisNode = new RedisNode(senIpAndPort[0].trim(), Integer.valueOf(senIpAndPort[1]));
                redisNodeSet.add(senRedisNode);
            }
            sentinelConfig.setSentinels(redisNodeSet);
            jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig, jedisPoolConfig);
        } else {
            //SpringBoot整合单机版redis
            if (serverArray.length == 1) {
                String[] ipAndPort = serverArray[0].split(":");
                RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(ipAndPort[0]);
                redisStandaloneConfiguration.setPort(Integer.valueOf(ipAndPort[1]));
                redisStandaloneConfiguration.setPassword(jedispoolProperties.getPassword());
                jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
                jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
            } else {
                //SpringBoot整合Redis-Cluster集群
                RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
                Set<RedisNode> nodes = new HashSet<RedisNode>();
                for (String ipPort : serverArray) {
                    String[] ipAndPort = ipPort.split(":");
                    nodes.add(new RedisNode(ipAndPort[0].trim(), Integer.valueOf(ipAndPort[1])));
                }
                redisClusterConfiguration.setClusterNodes(nodes);
                redisClusterConfiguration.setMaxRedirects(jedispoolProperties.getMaxRedirects());
                redisClusterConfiguration.setPassword(jedispoolProperties.getPassword());
                jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
            }
            //客户端超时时间单位是毫秒
            jedisConnectionFactory.setTimeout(jedispoolProperties.getTimeout());
            //测试连接redis其他数据库默认db0
            //jedisConnectionFactory.setDatabase(2);
        }

        return jedisConnectionFactory;
    }

    /**
     * 实例化 RedisTemplate 对象
     * 设置数据存入 redis 的序列化方式,并开启事务
     *
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setConnectionFactory(redisConnectionFactory);

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
