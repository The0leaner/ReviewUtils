package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/7/12.
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message:"+String.valueOf(userId);
    }
}
