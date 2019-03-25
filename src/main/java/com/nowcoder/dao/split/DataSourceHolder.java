package com.nowcoder.dao.split;

import org.springframework.stereotype.Component;

/**
 * 数据源管理
 *
 * @author wangleifu
 * @created 2019-03-05 9:40
 */
public class DataSourceHolder {
    private static ThreadLocal<DataSourceType> DBType = new ThreadLocal<>();

    public static DataSourceType getDBType() {
        DataSourceType type = DBType.get();
        return type == null ? DataSourceType.DB_MASTER : type;
    }

    public static void setDBType(DataSourceType type) {
        if (DataSourceType.DB_SLAVE.equals(type)) {
            DBType.set(DataSourceType.DB_SLAVE);
        }
        else {
            DBType.set(DataSourceType.DB_MASTER);
        }
    }

    public static void clear() {
        DBType.remove();
    }
}
