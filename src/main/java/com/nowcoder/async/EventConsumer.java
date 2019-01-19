package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.*;

/**
 * 处理事件队列
 *
 * @author wangleifu
 * @create 2018-12-20 21:13
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 找出所有实现了EventHandler接口的类 beans
         */
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            /**
             * 遍历每一个EventHandler类
              */
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                /**
                 * 获取当前EventHandler类支持的types
                 */
                List<EventType> types = entry.getValue().getSupportEventTypes();
                /**
                 * 将当前handler类和其所支持的type对应起来
                 */
                for (EventType type : types) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());

                }
            }
        }

        // 启动线程去消费事件
        ExecutorService service = newSingleThreadExecutor();
        service.submit(() -> {
                // 从队列一直消费
                while (true) {
                    String key = RedisKeyUtil.getEventQueneKey();
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String message: events) {
                        // 第一个元素是队列名字
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel model = JSON.parseObject(message, EventModel.class);
                        // 找到这个事件的处理handler列表
                        if (!config.containsKey(model.getEventType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(model.getEventType())) {
                            handler.doHandler(model);
                        }
                    }
                }
        });
        service.shutdown();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
