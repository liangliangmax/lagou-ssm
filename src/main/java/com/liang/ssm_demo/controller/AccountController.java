package com.liang.ssm_demo.controller;


import com.liang.spring.core.annotation.Autowired;
import com.liang.spring.webmvc.annotation.Controller;
import com.liang.spring.webmvc.annotation.RequestMapping;
import com.liang.ssm_demo.entity.Account;
import com.liang.ssm_demo.service.IAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @RequestMapping("/queryAll")
    public void queryAll(HttpServletRequest servletRequest, HttpServletResponse servletResponse){

        List<Account> accounts = accountService.queryAll();

        try {
            servletResponse.getWriter().write(accounts.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
