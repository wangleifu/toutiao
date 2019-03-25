package com.nowcoder.dao.split;

import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据库读写分离动态配置
 *
 * @author wangleifu
 * @created 2019-03-05 9:30
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(MyRoutingDataSource.class);

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("切换数据源到: " + DataSourceHolder.getDBType().getType());
        return DataSourceHolder.getDBType();
    }
}
