package com.zhangjc.mysql;

import com.zhangjc.mysql.utils.SqlToPoUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataGenerate {

    private Map<String, String> fieldMap = new LinkedHashMap<>();
    private Map<String, String> javaTypeMap = new LinkedHashMap<>();
    private Map<String, String> jdbcTypeMap = new LinkedHashMap<>();
    private Map<String, String> commentMap = new LinkedHashMap<>();

    private List<Map<String, Object>> fieldsList = new ArrayList<>();

    public void dataGenerate(ResultSet ret) throws SQLException {
        while (ret.next()) {
            Map<String, Object> fieldObj = new HashMap<>();

            String column_name = ret.getString(1);
            String data_type = ret.getString(2);
            String length = ret.getString(3);
            String column_comment = ret.getString(4);
            if (column_comment == null) {
                column_comment = "";
            }
            if (null != column_name && !column_name.equals("")) {
                this.fieldMap.put(column_name, SqlToPoUtil.replaceUnderlineAndfirstToUpper(column_name));
                this.commentMap.put(column_name, column_comment);

                fieldObj.put("columnName",column_name);
                fieldObj.put("columnComment",column_comment);

                if ("bigint".equals(data_type)) {
                    this.javaTypeMap.put(column_name, "Long");
                    this.jdbcTypeMap.put(column_name, "BIGINT");

                    fieldObj.put("columnType","BIGINT");
                } else if ("int".equals(data_type)) {
                    this.javaTypeMap.put(column_name, "Integer");
                    this.jdbcTypeMap.put(column_name, "INTEGER");

                    fieldObj.put("columnType","INTEGER");
                } else if ("datetime".equals(data_type)) {
                    this.javaTypeMap.put(column_name, "Date");
                    this.jdbcTypeMap.put(column_name, "TIMESTAMP");

                    fieldObj.put("columnType","TIMESTAMP");
                } else {
                    this.javaTypeMap.put(column_name, "String");
                    this.jdbcTypeMap.put(column_name, "VARCHAR");

                    fieldObj.put("columnType","VARCHAR");
                }
            }
            fieldObj.put("columnLength",length);
            this.fieldsList.add(fieldObj);
        }

    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Map<String, String> getJavaTypeMap() {
        return javaTypeMap;
    }

    public void setJavaTypeMap(Map<String, String> javaTypeMap) {
        this.javaTypeMap = javaTypeMap;
    }

    public Map<String, String> getJdbcTypeMap() {
        return jdbcTypeMap;
    }

    public void setJdbcTypeMap(Map<String, String> jdbcTypeMap) {
        this.jdbcTypeMap = jdbcTypeMap;
    }


    public Map<String, String> getCommentMap() {
        return commentMap;
    }

    public void setCommentMap(Map<String, String> commentMap) {
        this.commentMap = commentMap;
    }

    public List<Map<String, Object>> getFieldsList() {
        return fieldsList;
    }

    public void setFieldsList(List<Map<String, Object>> fieldsList) {
        this.fieldsList = fieldsList;
    }
}
