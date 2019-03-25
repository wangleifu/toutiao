package com.nowcoder.dao.split;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 默认情况下，所有的查询都走从库，插入/修改/删除走主库。我们通过方法名来区分操作类型（CRUD）
 *
 * @author wangleifu
 * @created 2019-03-05 21:12
 */
@Aspect
@Component
public class DataSourceAOP {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceAOP.class);

    @Pointcut("(execution(* com.nowcoder.dao..*.select*(..)) " +
            "|| execution(* com.nowcoder.dao..*.get*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.nowcoder.dao.split.Master) " +
            "|| execution(* com.nowcoder.dao..*.insert*(..)) " +
            "|| execution(* com.nowcoder.dao..*.add*(..)) " +
            "|| execution(* com.nowcoder.dao..*.update*(..)) " +
            "|| execution(* com.nowcoder.dao..*.edit*(..)) " +
            "|| execution(* com.nowcoder.dao..*.delete*(..)) " +
            "|| execution(* com.nowcoder.dao..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        logger.debug("切换数据源到slave");
        DataSourceHolder.setDBType(DataSourceType.DB_SLAVE);
    }

    @Before("writePointcut()")
    public void write() {
        logger.debug("切换数据源到master");
        DataSourceHolder.setDBType(DataSourceType.DB_MASTER);
    }
}
