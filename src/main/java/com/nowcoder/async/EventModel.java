package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type ;
    private int actorId;
    private int entityTypr;
    private int entityId;
    private int entityOwnerId;

    private Map<String , String> exts = new HashMap<>();

    /**
     *因为返回EventModel所以return this 可以把setExt方法返回值传给EventModel
     * @param key
     * @param value
     * @return
     */
    public EventModel setExt(String key , String value) {
        exts.put(key , value);
        return this;
    }


    public String getExt(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }


    /**
     * default constructor
     */
    public EventModel() {

    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityTypr() {
        return entityTypr;
    }

    public EventModel setEntityTypr(int entityTypr) {
        this.entityTypr = entityTypr;
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

    public EventModel (EventType type){
        this.type = type;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
