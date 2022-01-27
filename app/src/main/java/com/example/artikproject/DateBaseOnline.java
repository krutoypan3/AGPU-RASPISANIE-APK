package com.example.artikproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DateBaseOnline {

    private static java.sql.Connection con = null;
    private static final String url = "jdbc:jtds:sqlserver://"; // Драйвер
    private static final String serverName = "sql-serverartem.ddns.net"; // Адрес сервера
    private static final String portNumber = "54432"; // Порт
    private static final String databaseName = "RASPISANIE_AGPU"; // Название базы данных
    private static final String userName = "RASPISANIE_AGPU"; // Логин
    private static final String password = "RASPISANIE_AGPU"; // Пароль

    // Конструктор
    public DateBaseOnline() {
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
            System.out.println("АШИПКА in getConnection() в классе DateBaseOnline : " + e.getMessage());
        }
        return con;
    }

    public List<String[]> GetGroupList(String search_request){
        ResultSet resultSet;
        List<String[]> group_list = new ArrayList<>();
        try {
            con = this.getConnection();
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM item_list WHERE r_item_name LIKE '%" + search_request + "%'");
            while (resultSet.next()){
                group_list.add(new String[]{resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)});
            }
            closeConnection();
        }
        catch (Exception e){
            System.out.println("Ошибка в методе GetGroupList в модуле DateBaseOnline: " + e);
        }
        return group_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GetGroupRasp(String r_selectedItem_id, int week_id_upd, Context context){
        ResultSet resultSet;
        try {
            con = this.getConnection();
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM raspisanie WHERE r_group_code = '"+ r_selectedItem_id +"' AND r_week_number = '" + week_id_upd + "'");
            SQLiteDatabase sqLiteDatabaseS = new DataBaseLocal(context).getWritableDatabase(); //Подключение к базе данных
            while (resultSet.next()){
                Cursor r = sqLiteDatabaseS.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + resultSet.getString(1) + " AND r_week_number = " + resultSet.getString(3) + " AND r_week_day = " + resultSet.getString(2) + " AND r_para_number = " + resultSet.getString(4) + " AND " + " r_search_type = '" + resultSet.getString(13) + "'", null); // SELECT запрос
                ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                rowValues.put("r_group_code", resultSet.getString(1));
                rowValues.put("r_week_day", resultSet.getString(2));
                rowValues.put("r_week_number", resultSet.getString(3));
                rowValues.put("r_para_number", resultSet.getString(4));
                rowValues.put("r_name", resultSet.getString(5));
                rowValues.put("r_prepod", resultSet.getString(6));
                rowValues.put("r_group", resultSet.getString(7));
                rowValues.put("r_podgroup", resultSet.getString(8));
                rowValues.put("r_aud", resultSet.getString(9));
                rowValues.put("r_razmer", resultSet.getString(10));
                rowValues.put("r_week_day_name", resultSet.getString(11));
                rowValues.put("r_week_day_date", resultSet.getString(12));
                rowValues.put("r_search_type", resultSet.getString(13));
                rowValues.put("r_last_update", resultSet.getString(14));
                rowValues.put("r_color", resultSet.getString(15));
                rowValues.put("r_distant", resultSet.getString(16));
                if (r.getCount()==0){
                    sqLiteDatabaseS.insert("rasp_test1", null, rowValues);
                } // Если даной недели нет в базе
                else{ // Тут нужно быть аккуратнее с индексами
                    r.moveToFirst(); // У r в sqlite индексы начинаются с нуля(0..15), а у resultset mssql с единицы(1..16)
                    if (!(Objects.equals(resultSet.getString(5),r.getString(4))) |
                            !(Objects.equals(resultSet.getString(6), r.getString(5))) |
                            !(Objects.equals(resultSet.getString(16), r.getString(15)))|
                            !(Objects.equals(resultSet.getString(7), r.getString(6))) |
                            !(Objects.equals(resultSet.getString(8), r.getString(7))) |
                            !(Objects.equals(resultSet.getString(9), r.getString(8))) |
                            !(Objects.equals(resultSet.getString(10), r.getString(9)))){
                        sqLiteDatabaseS.delete("rasp_test1", "r_group_code = '" + r.getString(0) + "' AND r_week_number = '" + r.getString(2) + "' AND r_week_day = '" + r.getString(1) + "' AND r_para_number = '" + r.getString(3) + "' AND r_search_type = '" + r.getString(12) + "'", null);
                        sqLiteDatabaseS.insert("rasp_test1", null, rowValues);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            new addNotification(context, resultSet.getString(5) + " новое расписание!", "Расписание обновилось, скорее проверьте!");
                        }
                    }
                }
            }
            sqLiteDatabaseS.close();
            closeConnection();
        }
        catch (Exception e){
            System.out.println("Ошибка в методе GetGroupRasp в модуле DateBaseOnline: " + e);
        }
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