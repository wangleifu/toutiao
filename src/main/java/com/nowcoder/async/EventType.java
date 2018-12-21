package com.nowcoder.async;

/**
 * 队列中实体类型
 *
 * @author wangleifu
 * @create 2018-12-20 15:26
 */
public enum EventType {
    /**
     * 点赞消息
     */
    LIKE(0),
    /**
     * 评论消息
     */
    COMMENT(1),
    /**
     * 登录消息
     */
    LOGIN(2),
    /**
     * 邮件消息
     */
    MAIL(3);

    private int value;

    EventType (int value) {
        this.value = value;
    }

    public int getValue() { return value; }

}
