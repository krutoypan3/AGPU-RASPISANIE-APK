package com.example.artikproject.background_work.datebase;

import java.sql.DriverManager;

public class DateBase_Online {

    private static java.sql.Connection con = null;
    private static final String url = "jdbc:jtds:sqlserver://"; // Драйвер
    private static final String serverName = "sql-serverartem.ddns.net"; // Адрес сервера
    private static final String portNumber = "54432"; // Порт
    private static final String databaseName = "RASPISANIE_AGPU"; // Название базы данных
    private static final String userName = "RASPISANIE_AGPU"; // Логин
    private static final String password = "RASPISANIE_AGPU"; // Пароль

    // Constructor
    public DateBase_Online() {
    }

    private String getConnectionUrl() {
        return url + serverName + ":" + portNumber + ";database=" + databaseName;
    }

    private java.sql.Connection getConnection() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(getConnectionUrl(), userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


    private void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
            con = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}