package com.zhangjc.mysql;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

class CreateDataBaseDesign {

    public static void createDataBaseDesign(String basePath, String dbName, List<Map<String, Object>> list) {
        try {
            String path = basePath + "design";//所创建文件的路径
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();//创建目录
            }
            XWPFDocument doc = WordExportUtil
                    .exportWord07("templates/data-base-design.docx", list);
            FileOutputStream fos = new FileOutputStream(path + "/" + dbName + "-数据库设计说明书.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
