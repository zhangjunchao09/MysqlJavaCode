package com.zhangjc.mysql;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

class CreateCode {

    public static void createDataBaseDesign(String dbName, List<Map<String, Object>> list) {
        try {
            XWPFDocument doc = WordExportUtil
                    .exportWord07("templates/data-base-design.docx", list);
            FileOutputStream fos = new FileOutputStream("D:/autoCode/" + dbName + "数据库设计说明书.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
