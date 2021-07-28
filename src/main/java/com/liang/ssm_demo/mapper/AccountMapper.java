package com.liang.ssm_demo.mapper;

import com.liang.mybatis.core.annotation.Mapper;
import com.liang.ssm_demo.entity.Account;

import java.util.List;

@Mapper
public interface AccountMapper {

    List<Account> findAll();


}
