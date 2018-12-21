package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件模型, 所有信息保存在EventModel中
 *
 * @author wangleifu
 * @create 2018-12-20 15:05
 */
public class EventModel {
    /**
     * 触发类型
     */
    private EventType type;
    /**
     * 触发者
     */
    private int actorId;
    /**
     * 触发的对象 entityType + entityId
     */
    private int entityType;
    private int entityId;
    /**
     * 触发对象的拥有者
     */
    private int entityOwnerId;
    /**
     * 触发现在的数据（参数）
     */
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {}

    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }
    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventType getEventType() {
        return type;
    }

    public EventModel setEventType(EventType eventType) {
        this.type = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
