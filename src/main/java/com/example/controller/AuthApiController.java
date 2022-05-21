package com.example.controller;

import com.example.entity.resp.RestBean;
import com.example.service.AccountService;
import com.example.service.VerifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Resource
    VerifyService verifyService;
    @Resource
    AccountService accountService;

    @GetMapping("/verify-code")
    public RestBean<Void> verifyCode(@RequestParam("email") String email){
        try{
            verifyService.sendVerifyCode(email);
            return new RestBean<>(200,"邮件发送成功！");
        }catch (Exception e){
            return new RestBean<>(500,"邮件发送失败！");
        }
    }

    @PostMapping(value = "/register")
    public RestBean<Void> register(String username,
                             String password,
                             String email,
                             String verify){
        if(verifyService.doVerify(email,verify)){
            accountService.creatAccount(username,password);
            return new RestBean<>(200, "注册成功！");
        }
        else {
            return new RestBean<>(403,"验证码填写错误！注册失败!");
        }
    }

    @PostMapping("/login-success")
    public RestBean<Void> loginSuccess(){
        return new RestBean<>(200,"登录成功！");
    }


    @GetMapping("/logout-success")
    public RestBean<Void> logoutSuccess(){
        return new RestBean<>(200,"退出成功！");
    }

    @PostMapping("/login-failure")
    public RestBean<Void> loginFailure(){
        return new RestBean<>(304,"登录失败！,用户名或密码错误");
    }

    @RequestMapping("/access-deny")
    public RestBean<Void> accessDeny(){
        return new RestBean<>(401,"未验证请先进行登录!");
    }
}
