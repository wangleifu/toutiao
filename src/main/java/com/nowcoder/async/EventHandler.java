package com.nowcoder.async;

import java.util.List;

/**
 * 事件处理器规范接口
 *
 * @author wangleifu
 * @create 2018-12-20 20:58
 */
public interface EventHandler {
    /**
     * 功能函数，处理相应的事件
     * @param model 待处理的事件实体
     */
    void doHandler(EventModel model);

    /**
     *
     * @return 返回处理器可以处理的事件的类型
     */
    List<EventType> getSupportEventTypes();
}
