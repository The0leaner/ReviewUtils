package com.nowcoder.service;


import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
//依赖注入完成了
public class UserService {

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    //测试中通过了（向数据库中加入了具体数据）
    public Map<String, Object> register(String username , String password){
        Map<String , Object> map = new HashMap<String ,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msg" , "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg" , "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){

            map.put("msg" , "该用户名已被注册");
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);
        String ticket =addLoginTicket(user.getId());
        map.put("ticket" , ticket);
        return map;
    }

    public Map<String , Object> login(String username ,String password){
        Map<String , Object> map = new HashMap<String ,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msg" , "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg" , "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user == null){

            map.put("msg" , "用户名已存在");
        }
        if(WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg" , "密码错误");
            return map;
      }

      String ticket =addLoginTicket(user.getId());
        map.put("ticket" , ticket);
      return map;
    }

    public String addLoginTicket (int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*100 + now.getTime());
        ticket.setExpired(now);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-" ,""));
        //这里用随机数生成ticket，同时因为UUID会有“-”，所以必须replace
        loginTicketDAO.addTicket(ticket);

        return ticket.getTicket();
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public void Logout (String ticket){
        loginTicketDAO.updateStatus(ticket , 1);
    }
}
