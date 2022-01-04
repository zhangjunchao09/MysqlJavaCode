package com.zhangjc.mysql;

import com.zhangjc.mysql.jdbc.DBHelper;
import com.zhangjc.mysql.utils.PropertiesUtil;
import com.zhangjc.mysql.utils.SqlToPoUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MysqlToJava {


    private static String url;
    private static String driverName;
    private static String user;
    private static String password;

    private static String basePath; // 文件生成路径
    private static String pakage; // 代码包名

    private static String dbName;

    private static String tableName;
    private static String className;
    private static String primaryKeyField;
    private static String primaryKey;

    public static void main(String[] args) {
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
        className = SqlToPoUtil.toUpperCaseFirstOne(SqlToPoUtil.replaceUnderlineAndfirstToUpper(tableName));
        primaryKeyField = PropertiesUtil.getPropertiesByName("primaryKey");
        primaryKey = SqlToPoUtil.replaceUnderlineAndfirstToUpper(primaryKeyField);

//        singleTableToJavaCode();
//        multiTableToJavaCode();

        dataBaseDesign();
    }

    /**
     * 单表生成代码和表设计
     */
    public static void singleTableToJavaCode() {
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
    public static void multiTableToJavaCode() {
        List<Map<String, Object>> data = new ArrayList();

        String tableSql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where TABLE_SCHEMA =" + dbName + "'";
        DBHelper tableDb = new DBHelper(url, driverName, user, password, tableSql);//创建DBHelper对象
        try (ResultSet ret = tableDb.pst.executeQuery()) {
            while (ret.next()) {
                String table_name = ret.getString(1);
                DataGenerate dataGenerate = singleTableToDataGenerate(dbName, table_name);
                dataGenerate.createDao(basePath, pakage, className, primaryKey);
                dataGenerate.createService(basePath, pakage, className, primaryKey);
                dataGenerate.createServiceIml(basePath, pakage, className, primaryKey);
                dataGenerate.createDto(basePath, pakage, className);
                dataGenerate.createModel(basePath, pakage, className);
                dataGenerate.createMapper(basePath, pakage, className, tableName, primaryKey, primaryKeyField);
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
        DBHelper db = new DBHelper(url, driverName, user, password, sql);//创建DBHelper对象
        DataGenerate dataGenerate = new DataGenerate();
        try (ResultSet ret = db.pst.executeQuery()) {
            dataGenerate.dataGenerate(ret);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataGenerate;
    }

    /**
     * 多表生成数据库设计说明
     */
    public static void dataBaseDesign() {
        List<Map<String, Object>> data = new ArrayList();

        String tableSql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where TABLE_SCHEMA =" + dbName + "'";
        DBHelper tableDb = new DBHelper(url, driverName, user, password, tableSql);//创建DBHelper对象
        try (ResultSet ret = tableDb.pst.executeQuery()) {
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

        CreateCode.createDataBaseDesign(dbName, data);

    }


}
