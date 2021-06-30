package com.bilin.mybatis.readwrite.common;

import com.bilin.mybatis.readwrite.common.constant.DataSourceEnum;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Created by IDEA
 * User: mashaohua
 * Date: 2016-08-10 11:09
 * Desc:
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class}) })
public class DynamicPlugin implements Interceptor {
    protected static final Logger logger = LoggerFactory.getLogger(DynamicPlugin.class);
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";
    private static final Map<String, DataSourceEnum> cacheMap = new ConcurrentHashMap<>();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
       /* boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if(!synchronizationActive) {
        }*/
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        DataSourceEnum dynamicDataSourceGlobal = null;
        if((dynamicDataSourceGlobal = cacheMap.get(ms.getId())) == null) {
            //读方法
            if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    dynamicDataSourceGlobal = DataSourceEnum.WRITE_DATASOURCE;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    if(sql.matches(REGEX)) {
                        dynamicDataSourceGlobal = DataSourceEnum.WRITE_DATASOURCE;
                    } else {
                        dynamicDataSourceGlobal = DataSourceEnum.READ_DATASOURCE;
                    }
                }
            }else{
                dynamicDataSourceGlobal = DataSourceEnum.WRITE_DATASOURCE;
            }
            logger.warn("设置方法[{}] use [{}] Strategy, SqlCommandType [{}]..", ms.getId(), dynamicDataSourceGlobal.name(), ms.getSqlCommandType().name());
            cacheMap.put(ms.getId(), dynamicDataSourceGlobal);
        }
        RouteContextHolder.setDataSource(dynamicDataSourceGlobal.getValue());
        return invocation.proceed();
    }
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }
    @Override
    public void setProperties(Properties properties) {
        //
    }
}