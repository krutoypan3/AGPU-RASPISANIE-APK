package com.example.artikproject.background_work.datebase;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
            System.out.println("АШИПКА in getConnection() : " + e.getMessage());
        }
        return con;
    }

    public String[] check_update(){
        ResultSet resultSet;
        String[] version_info_db = {"","","",""};
        try {
            con = this.getConnection();
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT" +
                    " version_code\n" +
                    ",version_name\n" +
                    ",url_version\n" +
                    ",whats_new\n" +
                    " FROM check_update WHERE version_code=(SELECT MAX(version_code) FROM check_update)");
            while (resultSet.next()){
                version_info_db[0] = resultSet.getString(1);
                version_info_db[1] = resultSet.getString(2);
                version_info_db[2] = resultSet.getString(3);
                version_info_db[3] = resultSet.getString(4);
            }
            closeConnection();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return version_info_db;
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