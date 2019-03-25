package com.nowcoder.dao.split;

/**
 * 数据源类型
 *
 * @author wangleifu
 * @create 2019-03-05 15:39
 */
public enum DataSourceType {
    DB_MASTER("DB_MASTER"),
    DB_SLAVE("DB_SLAVE");

    private String type;
    DataSourceType(String type) {
        this.type = type;
    }
    public String getType() {
        return this.type;
    }
}
