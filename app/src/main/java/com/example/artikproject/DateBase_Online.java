package com.example.artikproject;

import android.os.StrictMode;

import java.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DateBase_Online {

    private java.sql.Connection con = null;
    private final String url = "jdbc:postgresql://"; // Драйвер
    private final String serverName = "sql-serverartem.ddns.net"; // Адрес сервера
    private final String instance = "ARTEM_HOME_SQL"; // Название сервера
    private final String portNumber = "54432"; // Порт
    private final String databaseName = "RASPISANIE_AGPU"; // Название базы данных
    private final String userName = "RASPISANIE_AGPU"; // Логин
    private final String password = "RASPISANIE_AGPU"; // Пароль

    // Constructor
    public DateBase_Online() {
    }

    private String getConnectionUrl() {
        return url + serverName + "\\" + instance + ":" + portNumber + ";database=" + databaseName;
    }

    private java.sql.Connection getConnection() {
        try {
            String url = "jdbc:postgresql://sql-serverartem.ddns.net/RASPISANIE_SERVER?user=RASPISANIE_USER&password=RASPISANIE_AGPU";
            con = DriverManager.getConnection(url);
            if (con != null) {
                System.out.println("Connection Successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("АШИПКА Trace in getConnection() : " + e.getMessage());
        }
        return con;
    }

    public void displayDbProperties() {
        java.sql.DatabaseMetaData dm = null;
        java.sql.ResultSet rs = null;
        try {
            con = this.getConnection();
            if (con != null) {
                dm = con.getMetaData();
                System.out.println("Driver Information");
                System.out.println("\tDriver Name: " + dm.getDriverName());
                System.out.println("\tDriver Version: " + dm.getDriverVersion());
                System.out.println("\nDatabase Information ");
                System.out.println("\tDatabase Name: " + dm.getDatabaseProductName());
                System.out.println("\tDatabase Version: " + dm.getDatabaseProductVersion());
                System.out.println("Avalilable Catalogs ");
                rs = dm.getCatalogs();
                while (rs.next()) {
                    System.out.println("\tcatalog: " + rs.getString(1));
                }
                rs.close();
                rs = null;
                closeConnection();
            } else {
                System.out.println("Error: No active Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dm = null;
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

    public static void main() throws Exception {
        DateBase_Online myDbTest = new DateBase_Online();
        myDbTest.displayDbProperties();
    }
}