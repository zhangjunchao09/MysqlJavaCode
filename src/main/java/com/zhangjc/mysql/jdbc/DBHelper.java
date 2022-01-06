package com.zhangjc.mysql.jdbc;

import com.zhangjc.mysql.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper implements AutoCloseable {

    private Connection conn = null;
    private PreparedStatement pst = null;

    public DBHelper(String sql) {
        try {
            Class.forName(Config.driverName);//指定连接类型
            conn = DriverManager.getConnection(Config.url, Config.user, Config.password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery() throws SQLException {
        return this.pst.executeQuery();
    }


    @Override
    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
