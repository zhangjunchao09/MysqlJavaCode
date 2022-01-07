package com.zhangjc.mysql;

import com.zhangjc.mysql.config.Config;
import com.zhangjc.mysql.utils.SqlToPoUtil;

public abstract class BaseToJava {

    public String basePath; // 文件生成路径
    public String pakage; // 代码包名

    public String dbName;

    public String tableName;
    public String className;
    public String primaryKeyField;
    public String primaryKey;

    public void init() {
        dbName = Config.dbName;
        pakage = Config.pakage;
        basePath = Config.basePath;
        tableName = Config.tableName;
        className = SqlToPoUtil.toUpperCaseFirstOne(SqlToPoUtil.replaceUnderlineAndfirstToUpper(tableName));
        primaryKeyField = Config.primaryKeyField;
        primaryKey = SqlToPoUtil.replaceUnderlineAndfirstToUpper(primaryKeyField);
    }

}
