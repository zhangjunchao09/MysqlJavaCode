package com.zhangjc.mysql;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.zhangjc.mysql.utils.FreeMarkeUtil;
import com.zhangjc.mysql.utils.SqlToPoUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CreateCode {

    static void createMapper(Map<String, String> fieldMap,
                             Map<String, String> jdbcTypeMap,
                             String basePath,
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

    static void createModel(Map<String, String> javaTypeMap, Map<String, String> commentMap, String basePath, String pakage, String className) {
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

    public static void createDto(Map<String, String> javaTypeMap, Map<String, String> commentMap, String basePath, String pakage, String className) {
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

    public static void createDao(String basePath, String pakage, String className, String primaryKey) {
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

    public static void createService(String basePath, String pakage, String className, String primaryKey) {
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

    public static void createServiceIml(String basePath, String pakage, String className, String primaryKey) {
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

    public static void createDataBaseDesign(List<Map<String, Object>> fields) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("fields", fields);
            XWPFDocument doc = WordExportUtil
                    .exportWord07("templates/data-base-design.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/autoCode/数据库设计说明书.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
