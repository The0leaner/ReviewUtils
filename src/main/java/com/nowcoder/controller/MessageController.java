package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.plugin2.message.Conversation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */@Controller
public class MessageController {
     @Autowired
    HostHolder hostHolder;

     @Autowired
    MessageService messageService;

     @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

      @RequestMapping(value = "/msg/addMessage" , method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
          try{
              if(hostHolder.getUser() == null) {
                  return WendaUtil.getJSONString(999 , "未登录");
              }
              User user = userService.selectByName(toName);
              if(user == null) {
                  return WendaUtil.getJSONString(1 , "用户不存在");
              }
              Message msg = new Message();
              msg.setContent(content);
              msg.setFromId(hostHolder.getUser().getId());
              msg.setToId(user.getId());
              msg.setCreatedDate(new Date());
              messageService.addMessage(msg);
              return WendaUtil.getJSONString(0 );
          }catch (Exception e){
               logger.error("增加站内信失败" + e.getMessage());
               return WendaUtil.getJSONString(1 , "插入站内信失败");
          }
      }

    @RequestMapping(path = {"/msg/list"} , method = {RequestMethod.GET})
    public String getConversationId(Model model) {
        try{
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>() ;
            List<Message> conversationList = messageService.getConversationList(localUserId , 0 ,10);
            for(Message msg : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation" , msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user" , user);
                vo.set("unread" , messageService.getConversationUnreadCount(localUserId ,msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations" , conversations);

        }catch(Exception e) {
            logger.error("增加站内信失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"} , method = {RequestMethod.GET})
    public String getConversationDetail(Model model , @RequestParam("conversationId") String conversationId ) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId , 0 , 10 );
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message" , message);
                vo.set("user" , userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages"  , messages);
        }catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }
}
