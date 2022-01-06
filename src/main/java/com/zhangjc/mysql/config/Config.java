package com.zhangjc.mysql.config;

import com.zhangjc.mysql.utils.PropertiesUtil;

public class Config {

    public static String url;
    public static String driverName;
    public static String user;
    public static String password;

    public static String basePath; // 文件生成路径
    public static String pakage; // 代码包名

    public static String dbName;

    public static String tableName;
    public static String primaryKeyField;


    static {
        /**
         * 数据库连接
         */
        url = PropertiesUtil.getPropertiesByName("url");
        driverName = PropertiesUtil.getPropertiesByName("driverName");
        user = PropertiesUtil.getPropertiesByName("user");
        password = PropertiesUtil.getPropertiesByName("password");

        dbName = PropertiesUtil.getPropertiesByName("dbName");

        /**
         * 包名和文件路径
         */
        pakage = PropertiesUtil.getPropertiesByName("pakage");
        basePath = PropertiesUtil.getPropertiesByName("basePath");


        /**
         * 表名和主键
         */
        tableName = PropertiesUtil.getPropertiesByName("tableName");
        primaryKeyField = PropertiesUtil.getPropertiesByName("primaryKey");
    }
}
