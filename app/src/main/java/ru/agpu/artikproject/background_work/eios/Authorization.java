package ru.agpu.artikproject.background_work.eios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Authorization extends Thread{
    String login;
    String password;
    private String ACCESS_TOKEN;
    private String MSG_ERROR;

    public Authorization(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public void run(){
        try {// Проверяем переданные параметры, чтобы они не были пустыми
            if (login.equals("") || password.equals(""))
                ACCESS_TOKEN = "";
            URL url = new URL("http://plany.agpu.net/api/tokenauth"); // Указываем ссылку на страницу авторизации
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Открываем поключение
            connection.setRequestMethod("POST"); // Устанавливаем тип запроса - POST
            connection.setDoOutput(true); // Устанавливаем ожидание данных для вывода
            connection.setRequestProperty("Content-Type", "application/json"); // Указываем тип необходимого контента

            // Добавляем наши логин и пароль в дату
            String data = "{\n  \"userName\": \"" + login + "\",\n  \"password\": \"" + password + "\"}";
            // Переводим дату в байты
            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            // Запускаем прослушку на выходное подключение
            OutputStream stream = connection.getOutputStream();
            stream.write(out); // И отправляем дату
            if (connection.getResponseCode() == 200) { // Если запрос прошел
                InputStream inputStream = connection.getInputStream(); // Получаем информацию
                String text = new BufferedReader( // И переводим ее в текст
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining("\n"));
                // Далее извлекаем токен в переменную accessToken
                String accessToken = text.split("\"accessToken\":")[1].split(",")[0];
                if (accessToken.equals("null")){ // Если токен пустой
                    ACCESS_TOKEN = "";
                    MSG_ERROR = text.split("msg\":\"")[1].split("\",")[0];
                }
                else // Если токен не пустой, удаляем кавычки в начале и конце
                    ACCESS_TOKEN = accessToken.replace("\"", "");
            }
            else
                ACCESS_TOKEN = "";
        } catch (Exception e) {
            e.printStackTrace();
            ACCESS_TOKEN = "";
        }
    }
    public String getACCESS_TOKEN(){
        return ACCESS_TOKEN;
    }
    public String getMSG_ERROR(){
        return MSG_ERROR;
    }
}
