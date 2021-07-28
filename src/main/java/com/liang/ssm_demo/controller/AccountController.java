package com.liang.ssm_demo.controller;


import com.liang.spring.core.annotation.Autowired;
import com.liang.spring.webmvc.annotation.Controller;
import com.liang.spring.webmvc.annotation.RequestMapping;
import com.liang.ssm_demo.entity.Account;
import com.liang.ssm_demo.service.IAccountService;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @RequestMapping("/queryAll")
    public List<Account> queryAll(){

        List<Account> accounts = accountService.queryAll();

        return accounts;
    }

}
