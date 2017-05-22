package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;


import com.nowcoder.util.MailSender;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MailSender mailSender;

    @Override
    public void doHander(EventModel eventModel) {
        // xxxx判断发现这个用户登陆异常
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", eventModel.getExt("username"));

        //(eventmodel.getExt("email"), "登陆IP异常", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return  Arrays.asList(EventType.LOGIN);
    }
}
