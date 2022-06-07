package ru.agpu.artikproject.background_work.eios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import ru.agpu.artikproject.background_work.TextDetranslit;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;

/**
 * Класс, который получает информацию о пользователю по токену
 */
public class GetUserInfo extends Thread{
    private String USER_ID; // ID пользователя
    private String FIO; // Фамилия Имя Отчество [Я СКАЗАЛ ФАМИЛИЯ ИМЯ ОТЧЕСТВО!]
    private String FIRST_NAME; // Имя
    private String LAST_NAME; // Фамилия
    private String MIDDLE_NAME; // Отчество
    private String GROUP_NAME; // Имя группы
    private String GROUP_ID; // ID Группы
    private final String accessToken; // Токен пользователя

    public GetUserInfo(String accessToken){
        this.accessToken = accessToken;
    }

    @Override
    public void run() {
        try {
            URL url = new URL("http://plany.agpu.net/api/tokenauth");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Открываем соединение
            connection.setRequestMethod("GET"); // Но теперь это будет GET - запрос
            connection.setRequestProperty("Authorization", "Bearer " + accessToken); // Устанавливаем параметры
            connection.setRequestProperty("Cookie", "authToken=" + accessToken); // С полученным токеном
            InputStream inputStream = connection.getInputStream(); // Получаем информацию
            String text = new BufferedReader( // И переводим ее в текст
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            // Извлекаем ФИО студента
            FIRST_NAME = text.split("\"first_name\":\"")[1].split("\"")[0];
            LAST_NAME = text.split("\"last_name\":\"")[1].split("\"")[0];
            MIDDLE_NAME = text.split("\"middle_name\":\"")[1].split("\"")[0];

            FIO = FIRST_NAME + " " + LAST_NAME + " " + MIDDLE_NAME;

            // Извлекаем ID студента
            USER_ID = text.split("\"userID\":")[1].split(",\"")[0];

            url = new URL("http://plany.agpu.net/api/UserInfo/Student?studentID=" + USER_ID);
            connection = (HttpURLConnection) url.openConnection(); // Открываем соединение
            connection.setRequestMethod("GET"); // Но теперь это будет GET - запрос
            connection.setRequestProperty("Authorization", "Bearer " + accessToken); // Устанавливаем параметры
            connection.setRequestProperty("Cookie", "authToken=" + accessToken); // С полученным токеном
            inputStream = connection.getInputStream(); // Получаем информацию
            text = new BufferedReader( // И переводим ее в текст
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            GROUP_NAME = text.split("\"group\":")[1].split("item1\":\"")[1].split("\"")[0];
            GROUP_NAME = new TextDetranslit().detranslit(GROUP_NAME);
            for (int i = 0; i < GetFullGroupList_Online.faculties_group_name.size(); i++)
                for (int j = 0; j < GetFullGroupList_Online.faculties_group_name.get(i).size(); j++)
                    if (GetFullGroupList_Online.faculties_group_name.get(i).get(j).item.equalsIgnoreCase(GROUP_NAME)) {
                        GROUP_ID = GetFullGroupList_Online.faculties_group_id.get(i).get(j).item;
                        return;
                    }
            GROUP_ID = "-1"; // TODO это затычка, которая сработает если все плохо (не будет найден id группы)
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUserId(){
        return USER_ID;
    }

    public String getUserFIO(){
        return FIO;
    }

    public String getUserFirstName(){
        return FIRST_NAME;
    }

    public String getUserLastName(){
        return LAST_NAME;
    }

    public String getUserMiddleName(){
        return MIDDLE_NAME;
    }

    public String getGroupName(){
        return GROUP_NAME;
    }

    public String getGroupId(){
        return GROUP_ID;
    }
}
