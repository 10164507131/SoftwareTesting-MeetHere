package com.meethere.controller.user;

import com.meethere.entity.User;
import com.meethere.service.UserService;
import com.meethere.service.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.PastOrPresent;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signUp(){
        return "signup";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }




    @PostMapping("/loginCheck.do")
    public void login(String userID,String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user=userService.checkLogin(userID,password);
        if(user!=null){
            if(user.getIsadmin()==0){
            request.getSession().setAttribute("user",user);
            response.sendRedirect("/index");
                System.out.println("user login!");
            }
            else if(user.getIsadmin()==1){
                request.getSession().setAttribute("user",user);
                response.sendRedirect("/admin_index");
                System.out.println("admin login!");
            }

        }
        else
            throw new LoginException("登录失败，用户名或密码错误！");

    }

    @PostMapping("/register.do")
    public String register(User user,HttpServletResponse response) throws IOException {
        userService.create(user);
        return "login";
    }

    @GetMapping("/logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("user");
        System.out.println("log out success!");
        response.sendRedirect("/index");
    }

    @PostMapping("/checkUserID.do")
    @ResponseBody
    public boolean checkUSerID(String userID){
        int count=userService.countUserID(userID);
        return count < 1;
    }

    @PostMapping("/updateUser.do")
    @ResponseBody
    public boolean updateUser(){
        return true;
    }


    @GetMapping("/user_info")
    public String user_info(Model model){
        return "user_info";
    }
}
