package com.nowcoder.configuration;

import com.nowcoder.dao.TestConnectionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: wang
 * @Desciption: 维持 mysql 连接
 * @Date: Created in 11:21 2019/1/6
 * @Modified By:
 **/
@Component
public class MysqlConnectionKeepConfiguration {

    @Autowired
    private TestConnectionDao testConnectionDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Scheduled(cron = "0 0 0,7,14,21 * * ?")
    public void scheduledQuery(){
        logger.info("---{} 发出 test 查询请求---", new Date());
        testConnectionDao.query();
    }

}
