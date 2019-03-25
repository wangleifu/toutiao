package com.nowcoder.async.util;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventType;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 异步处理的工具类
 *
 * @author wangleifu
 * @created 2019-03-08 16:46
 */
public class AsyncUtil {

    public static void getEventHandlerBeans(ApplicationContext applicationContext, Map<EventType, List<EventHandler>> config) {
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
    }
}
