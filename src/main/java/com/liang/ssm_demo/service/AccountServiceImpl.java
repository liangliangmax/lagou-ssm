package com.liang.ssm_demo.service;

import com.liang.spring.core.annotation.Autowired;
import com.liang.spring.core.annotation.Service;
import com.liang.ssm_demo.entity.Account;
import com.liang.ssm_demo.mapper.AccountMapper;

import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService{

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> queryAll() {

        List<Account> all = accountMapper.findAll();

        System.out.println(all);

        return all;
    }
}
