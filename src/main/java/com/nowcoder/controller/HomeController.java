package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/17.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;
private List<ViewObject> getQuestionList(int userId , int offset , int limit) {
    List<Question> questionList = questionService.getLatestQuestion(userId, offset, limit);
    List<ViewObject> vos = new ArrayList<>();
    for (Question question : questionList) {
        ViewObject vo = new ViewObject();
        vo.set("qusetion", question);
        vo.set("user", userService.getUser(question.getUserId()));
        vos.add(vo);
    }
    return vos;
}
    @RequestMapping(path = {"/user/{userId}" }, method = {RequestMethod.GET ,RequestMethod.POST})

    public String user(Model model , @PathVariable("userId") int userId) {
    model.addAttribute("vos" , getQuestionList(userId , 0 , 10));
    return "index";
    }
    @RequestMapping(path = {"/user/{userId}" }, method = {RequestMethod.GET,RequestMethod.POST})

    public String index(Model model,@RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos" , getQuestionList(0 , 0 , 10));
        return "index";
    }
//    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
//
//    public String index(Model model) {
//        List<Question> questionList  = questionService.getLatestQuestion(0, 0 , 10);
//        List<ViewObject> vos = new ArrayList<>();
//        for (Question question :questionList){
//            ViewObject vo = new ViewObject();
//            vo.set("qusetion" , question);
//            vo.set("user" , userService.getUser(question.getUserId()));
//            vos.add(vo);
//
//        }
////        model.addAttribute("questions" , questionList);
//        model.addAttribute("vos" , vos);
//        return "index";
//    }

}
