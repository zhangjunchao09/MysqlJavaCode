package com.zhangjc.mysql;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.zhangjc.mysql.utils.FreeMarkeUtil;
import com.zhangjc.mysql.utils.SqlToPoUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

                fieldObj.put("columnName", column_name);
                fieldObj.put("columnComment", column_comment);

                if ("bigint".equals(data_type)) {
                    this.javaTypeMap.put(column_name, "Long");
                    this.jdbcTypeMap.put(column_name, "BIGINT");

                    fieldObj.put("columnType", "BIGINT");
                } else if ("int".equals(data_type)) {
                    this.javaTypeMap.put(column_name, "Integer");
                    this.jdbcTypeMap.put(column_name, "INTEGER");

                    fieldObj.put("columnType", "INTEGER");
                } else if ("datetime".equals(data_type)) {
                    this.javaTypeMap.put(column_name, "Date");
                    this.jdbcTypeMap.put(column_name, "TIMESTAMP");

                    fieldObj.put("columnType", "TIMESTAMP");
                } else {
                    this.javaTypeMap.put(column_name, "String");
                    this.jdbcTypeMap.put(column_name, "VARCHAR");

                    fieldObj.put("columnType", "VARCHAR");
                }
            }
            fieldObj.put("columnLength", length);
            this.fieldsList.add(fieldObj);
        }

    }

    public void createMapper(String basePath,
                             String pakage,
                             String className,
                             String tableName,
                             String primaryKey,
                             String primaryKeyField) {
        String path = basePath + "mapper";//所创建文件的路径
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//创建目录
        }
        String fileName = className + "Mapper.xml";//文件名及类型
        Map<String, Object> root = new HashMap<>();
        root.put("className", className);
        root.put("tableName", tableName);
        root.put("pakage", pakage);
        Map<String, String> typeMap = new HashMap<>();
        Set<String> keys = jdbcTypeMap.keySet();
        for (Object key : keys) {
            String k = (String) key;
            String javaType = jdbcTypeMap.get(k);
            typeMap.put(k, javaType);
        }
        root.put("fieldMap", fieldMap);
        root.put("typeMap", typeMap);
        root.put("primaryKey", primaryKey);
        root.put("primaryKeyField", primaryKeyField);
        FreeMarkeUtil.fprint(path, "mapper.ftl", root, fileName);

    }

    public void createModel(String basePath, String pakage, String className) {
        String path = basePath + "model";//所创建文件的路径
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//创建目录
        }
        String fileName = className + "Model.java";//文件名及类型
        Map<String, Object> root = new HashMap<>();
        root.put("className", className);
        root.put("lowclassName", SqlToPoUtil.toLowerCaseFirstOne(className));
        root.put("pakage", pakage);
        Map<String, String> typeMap = new HashMap<>();
        Set<String> keys = javaTypeMap.keySet();
        for (Object key : keys) {
            String k = (String) key;
            String field = SqlToPoUtil.replaceUnderlineAndfirstToUpper(k);
            String javaType = javaTypeMap.get(k);
            typeMap.put(field, javaType);
            commentMap.put(field, commentMap.get(k));
        }
        root.put("typeMap", typeMap);
        root.put("commentMap", commentMap);
        FreeMarkeUtil.fprint(path, "model.ftl", root, fileName);
    }

    public void createDto(String basePath, String pakage, String className) {
        String path = basePath + "dto";//所创建文件的路径
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//创建目录
        }
        String fileName = className + "Dto.java";//文件名及类型
        Map<String, Object> root = new HashMap<>();
        root.put("className", className);
        root.put("lowclassName", SqlToPoUtil.toLowerCaseFirstOne(className));
        root.put("pakage", pakage);
        Map<String, String> typeMap = new HashMap<>();
        Set<String> keys = javaTypeMap.keySet();
        for (Object key : keys) {
            String k = (String) key;
            String field = SqlToPoUtil.replaceUnderlineAndfirstToUpper(k);
            String javaType = javaTypeMap.get(k);
            if (javaType.equals("Date")) {
                javaType = "String";
            }
            if (javaType.equals("Long")) {
                javaType = "String";
            }
            typeMap.put(field, javaType);
            commentMap.put(field, commentMap.get(k));
        }
        root.put("typeMap", typeMap);
        root.put("commentMap", commentMap);
        FreeMarkeUtil.fprint(path, "dto.ftl", root, fileName);
    }

    public void createDao(String basePath, String pakage, String className, String primaryKey) {
        String path = basePath + "dao";//所创建文件的路径
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//创建目录
        }
        String fileName = className + "Mapper.java";//文件名及类型
        Map<String, Object> root = new HashMap<>();
        root.put("className", className);
        root.put("lowclassName", SqlToPoUtil.toLowerCaseFirstOne(className));
        root.put("pakage", pakage);
        root.put("primaryKey", primaryKey);
        FreeMarkeUtil.fprint(path, "dao.ftl", root, fileName);
    }

    public void createService(String basePath, String pakage, String className, String primaryKey) {
        String path = basePath + "service";//所创建文件的路径
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//创建目录
        }
        String fileName = "I" + className + "Service.java";//文件名及类型
        Map<String, Object> root = new HashMap<>();
        root.put("className", className);
        root.put("lowclassName", SqlToPoUtil.toLowerCaseFirstOne(className));
        root.put("pakage", pakage);
        root.put("primaryKey", primaryKey);
        FreeMarkeUtil.fprint(path, "service.ftl", root, fileName);
    }

    public void createServiceIml(String basePath, String pakage, String className, String primaryKey) {
        String path = basePath + "serviceiml";//所创建文件的路径
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//创建目录
        }
        String fileName = className + "Service.java";//文件名及类型
        Map<String, Object> root = new HashMap<>();
        root.put("className", className);
        root.put("lowclassName", SqlToPoUtil.toLowerCaseFirstOne(className));
        root.put("pakage", pakage);
        root.put("primaryKey", primaryKey);
        FreeMarkeUtil.fprint(path, "serviceiml.ftl", root, fileName);

    }

    public void createDataBaseDesign(String tableName) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", tableName);
            map.put("fields", fieldsList);
            XWPFDocument doc = WordExportUtil
                    .exportWord07("templates/data-base-design.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/autoCode/" + tableName + "-数据库设计说明书.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Map<String, Object>> getFieldsList() {
        return fieldsList;
    }

    public void setFieldsList(List<Map<String, Object>> fieldsList) {
        this.fieldsList = fieldsList;
    }
}
