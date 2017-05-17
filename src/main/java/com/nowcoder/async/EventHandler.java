package com.nowcoder.async;


import java.util.List;

public interface EventHandler {
    void doHander(EventModel eventModel) ;

    List<EventType> getSupportEventType();
}
