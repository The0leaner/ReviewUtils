package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2017/4/18.
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users = new ThreadLocal<>();
    //看起来只有一个变量，其实每个线程中都包含一个User的拷贝 ，占用内存不同，但可以通过一个公共接口来访问
    //Map<ThreadId , User>根据所在线程找寻所在线程的User

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
         users.set(user);
    }

    public void clean(){
        users.remove();
    }
}
