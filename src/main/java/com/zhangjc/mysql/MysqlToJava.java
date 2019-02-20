package com.zhangjc.mysql;

import com.zhangjc.mysql.jdbc.DBHelper;
import com.zhangjc.mysql.utils.PropertiesUtil;
import com.zhangjc.mysql.utils.SqlToPoUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MysqlToJava {

    public static void main(String[] args) {
        String tableName = PropertiesUtil.getPropertiesByName("tableName");
        String dbName = PropertiesUtil.getPropertiesByName("dbName");
        String pakage = PropertiesUtil.getPropertiesByName("pakage");
        String basePath = PropertiesUtil.getPropertiesByName("basePath");
        String className = SqlToPoUtil.replaceUnderlineAndfirstToUpper(tableName).substring(1);
        String url = PropertiesUtil.getPropertiesByName("url");
        String name = PropertiesUtil.getPropertiesByName("name");
        String user = PropertiesUtil.getPropertiesByName("user");
        String password = PropertiesUtil.getPropertiesByName("password");
        String sql = "select column_name,data_type,character_maximum_length,column_comment " +
                "from information_schema.columns where table_schema ='" + dbName + "'  and table_name = '" + tableName + "' order by data_type";//SQL语句
        DBHelper db = new DBHelper(url, name, user, password, sql);//创建DBHelper对象
        ResultSet ret = null;
        try {
            ret = db.pst.executeQuery();//执行语句，得到结果集
            Map<String, String> fieldMap = new LinkedHashMap<>();
            Map<String, String> javaTypeMap = new LinkedHashMap<>();
            Map<String, String> jdbcTypeMap = new LinkedHashMap<>();
            while (ret.next()) {
                String column_name = ret.getString(1);
                String data_type = ret.getString(2);
                if (null != column_name && !column_name.equals("")) {
                    fieldMap.put(column_name, SqlToPoUtil.replaceUnderlineAndfirstToUpper(column_name));
                    if ("bigint".equals(data_type)) {
                        javaTypeMap.put(column_name, "Long");
                        jdbcTypeMap.put(column_name, "BIGINT");
                    } else if ("int".equals(data_type)) {
                        javaTypeMap.put(column_name, "Integer");
                        jdbcTypeMap.put(column_name, "INTEGER");
                    } else if ("datetime".equals(data_type)) {
                        javaTypeMap.put(column_name, "Date");
                        jdbcTypeMap.put(column_name, "TIMESTAMP");
                    } else {
                        javaTypeMap.put(column_name, "String");
                        jdbcTypeMap.put(column_name, "VARCHAR");
                    }
                }
            }
            CreateCode.createDao(basePath, pakage, className);
            CreateCode.createService(basePath, pakage, className);
            CreateCode.createServiceIml(basePath, pakage, className);
            CreateCode.createModel(javaTypeMap, basePath, pakage, className);
            CreateCode.createPo(javaTypeMap, basePath, pakage, className);
            CreateCode.createMapper(fieldMap, jdbcTypeMap, basePath, pakage, className, tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != ret) {
                try {
                    ret.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != db) {
                db.close();
            }
        }
    }
}