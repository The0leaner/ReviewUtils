package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;

import com.nowcoder.util.RedisKeyUtil;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean , ApplicationContextAware{
    private Map<EventType , List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;
    //每一个event过来就关心它对应的那一个eventhandler
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String , EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        //找到所有的eventhandler的实现类
        if(beans != null){
            for (Map.Entry<String , EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventType();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0 , key);
                    for (String message : events) {
                        if (message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject()
                    }
                }
            }
        })
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {

    }
}
