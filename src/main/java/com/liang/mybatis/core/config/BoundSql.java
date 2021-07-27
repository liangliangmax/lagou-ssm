package com.liang.mybatis.core.config;

import com.liang.mybatis.core.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

public class BoundSql {

    private String sqlText;

    private List<ParameterMapping> parameterMappingsList = new ArrayList<>();


    public BoundSql(String sqlText, List<ParameterMapping> parameterMappingsList) {
        this.sqlText = sqlText;
        this.parameterMappingsList = parameterMappingsList;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappingsList() {
        return parameterMappingsList;
    }

    public void setParameterMappingsList(List<ParameterMapping> parameterMappingsList) {
        this.parameterMappingsList = parameterMappingsList;
    }
}
