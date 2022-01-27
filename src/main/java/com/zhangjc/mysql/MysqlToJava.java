package com.zhangjc.mysql;

import com.zhangjc.mysql.jdbc.DBHelper;
import com.zhangjc.mysql.utils.SqlToPoUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MysqlToJava extends BaseToJava {

    String tableSql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where TABLE_SCHEMA ='dbName'";
    String columnSql = "select column_name,data_type,character_maximum_length,column_comment " +
            "from information_schema.columns where table_schema ='dbName' and table_name = 'tableName' order by data_type";//SQL语句

    public MysqlToJava() {
        init();
    }

    /**
     * 单表生成代码和表设计
     */
    public void singleTableToJavaCode() {
        String class_name = SqlToPoUtil.toUpperCaseFirstOne(SqlToPoUtil.replaceUnderlineAndfirstToUpper(tableName));
        singleTableToJavaCode(dbName, tableName, class_name);
    }

    /**
     * 单表生成代码和表设计
     */
    public void singleTableToJavaCode(String db_name, String table_name, String class_name) {
        DataGenerate dataGenerate = singleTableToDataGenerate(db_name, table_name);

        dataGenerate.createDao(basePath, pakage, class_name, primaryKey);
        dataGenerate.createService(basePath, pakage, class_name, primaryKey);
        dataGenerate.createServiceIml(basePath, pakage, class_name, primaryKey);
        dataGenerate.createController(basePath, pakage, class_name, primaryKey);
        dataGenerate.createDto(basePath, pakage, class_name);
        dataGenerate.createModel(basePath, pakage, class_name);
        dataGenerate.createMapper(basePath, pakage, class_name, table_name, primaryKey, primaryKeyField);
        dataGenerate.createDataBaseDesign(basePath, table_name);
    }


    /**
     * 多表生成代码
     */
    public void multiTableToJavaCode() {

        String sql = tableSql.replace("dbName", dbName);

        try (DBHelper tableDb = new DBHelper(sql);
             ResultSet ret = tableDb.executeQuery()) {
            while (ret.next()) {
                String table_name = ret.getString(1);
                String class_name = SqlToPoUtil.toUpperCaseFirstOne(SqlToPoUtil.replaceUnderlineAndfirstToUpper(table_name));
                singleTableToJavaCode(dbName, table_name, class_name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单表生成代码和表设计
     */
    public DataGenerate singleTableToDataGenerate(String db_name, String table_name) {
        String sql = columnSql.replace("dbName", db_name).replace("tableName", table_name);
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
        String sql = tableSql.replace("dbName", dbName);

        try (DBHelper tableDb = new DBHelper(sql);
             ResultSet ret = tableDb.executeQuery()) {
            while (ret.next()) {
                String table_name = ret.getString(1);
                String table_comment = ret.getString(2);
                if (null == table_comment) {
                    table_comment = " ";
                }
                DataGenerate dataGenerate = singleTableToDataGenerate(dbName, table_name);
                List<Map<String, Object>> fieldsList = dataGenerate.getFieldsList();

                Map<String, Object> map = new HashMap<>();
                map.put("tableName", table_name);
                map.put("tableComment", table_comment);
                map.put("fields", fieldsList);

                data.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CreateDataBaseDesign.createDataBaseDesign(basePath, dbName, data);

    }


}
