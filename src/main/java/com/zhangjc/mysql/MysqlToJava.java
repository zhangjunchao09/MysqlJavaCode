package com.zhangjc.mysql;

import com.zhangjc.mysql.config.Config;
import com.zhangjc.mysql.jdbc.DBHelper;
import com.zhangjc.mysql.utils.SqlToPoUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MysqlToJava {

    public String basePath; // 文件生成路径
    public String pakage; // 代码包名

    public String dbName;

    public String tableName;
    public String className;
    public String primaryKeyField;
    public String primaryKey;

    public MysqlToJava() {
        dbName = Config.dbName;
        pakage = Config.pakage;
        basePath = Config.basePath;
        tableName = Config.tableName;
        className = SqlToPoUtil.toUpperCaseFirstOne(SqlToPoUtil.replaceUnderlineAndfirstToUpper(tableName));
        primaryKeyField = Config.primaryKeyField;
        primaryKey = SqlToPoUtil.replaceUnderlineAndfirstToUpper(primaryKeyField);

    }

    /**
     * 单表生成代码和表设计
     */
    public void singleTableToJavaCode() {
        DataGenerate dataGenerate = singleTableToDataGenerate(dbName, tableName);

        dataGenerate.createDao(basePath, pakage, className, primaryKey);
        dataGenerate.createService(basePath, pakage, className, primaryKey);
        dataGenerate.createServiceIml(basePath, pakage, className, primaryKey);
        dataGenerate.createDto(basePath, pakage, className);
        dataGenerate.createModel(basePath, pakage, className);
        dataGenerate.createMapper(basePath, pakage, className, tableName, primaryKey, primaryKeyField);
        dataGenerate.createDataBaseDesign(tableName);
    }

    /**
     * 多表生成代码
     */
    public void multiTableToJavaCode() {

        String tableSql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where TABLE_SCHEMA ='" + dbName + "'";

        try (DBHelper tableDb = new DBHelper(tableSql);
             ResultSet ret = tableDb.executeQuery()) {
            while (ret.next()) {
                String table_name = ret.getString(1);
                String class_name = SqlToPoUtil.toUpperCaseFirstOne(SqlToPoUtil.replaceUnderlineAndfirstToUpper(table_name));

                DataGenerate dataGenerate = singleTableToDataGenerate(dbName, table_name);

                dataGenerate.createDao(basePath, pakage, class_name, primaryKey);
                dataGenerate.createService(basePath, pakage, class_name, primaryKey);
                dataGenerate.createServiceIml(basePath, pakage, class_name, primaryKey);
                dataGenerate.createDto(basePath, pakage, class_name);
                dataGenerate.createModel(basePath, pakage, class_name);
                dataGenerate.createMapper(basePath, pakage, class_name, table_name, primaryKey, primaryKeyField);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单表生成代码和表设计
     */
    public static DataGenerate singleTableToDataGenerate(String db_name, String table_name) {
        String sql = "select column_name,data_type,character_maximum_length,column_comment " +
                "from information_schema.columns where table_schema ='" + db_name + "'  and table_name = '" + table_name + "' order by data_type";//SQL语句

        DataGenerate dataGenerate = new DataGenerate();
        try (DBHelper db = new DBHelper(sql);
             ResultSet ret = db.executeQuery()) {
            dataGenerate.dataGenerate(ret);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataGenerate;
    }

    /**
     * 多表生成数据库设计说明
     */
    public void dataBaseDesign() {
        List<Map<String, Object>> data = new ArrayList();

        String tableSql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where TABLE_SCHEMA ='" + dbName + "'";

        try (DBHelper tableDb = new DBHelper(tableSql);
             ResultSet ret = tableDb.executeQuery()) {
            while (ret.next()) {
                String table_name = ret.getString(1);
//                String table_comment = ret.getString(2);
                DataGenerate dataGenerate = singleTableToDataGenerate(dbName, table_name);
                List<Map<String, Object>> fieldsList = dataGenerate.getFieldsList();

                Map<String, Object> map = new HashMap<>();
                map.put("tableName", tableName);
                map.put("fields", fieldsList);
                data.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CreateDataBaseDesign.createDataBaseDesign(dbName, data);

    }


}
