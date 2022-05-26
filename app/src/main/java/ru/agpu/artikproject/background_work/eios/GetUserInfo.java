package ru.agpu.artikproject.background_work.eios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;

public class GetUserInfo extends Thread{
    private String USER_ID;
    private String FIO;
    private String FIRST_NAME;
    private String LAST_NAME;
    private String MIDDLE_NAME;
    private String GROUP_NAME;
    private String GROUP_ID;
    private final String accessToken;

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
            GROUP_NAME = detranslit(GROUP_NAME);
            for (int i = 0; i < GetFullGroupList_Online.faculties_group_name.size(); i++)
                for (int j = 0; j < GetFullGroupList_Online.faculties_group_name.get(i).size(); j++)
                    if (GetFullGroupList_Online.faculties_group_name.get(i).get(j).item.equalsIgnoreCase(GROUP_NAME)) {
                        GROUP_ID = GetFullGroupList_Online.faculties_group_id.get(i).get(j).item;
                        return;
                    }
            GROUP_ID = "69"; // TODO это затычка, которая сработает если все плохо
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Если вы читаете это то значит и на вашем берегу попался *плохой* человек который вбивает
     * транслитом текст, который не стоило бы. Дай боже вам здоровья. Аминь!
     * @param text Текст, который вам нужно *исправить*
     * @return Исправленный текст
     */
    private String detranslit(String text){
        text = text.replace("A", "А");
        text = text.replace("B", "В");
        text = text.replace("C", "С");
        text = text.replace("E", "Е");
        text = text.replace("H", "Н");
        text = text.replace("K", "К");
        text = text.replace("M", "М");
        text = text.replace("O", "О");
        text = text.replace("P", "Р");
        text = text.replace("T", "Т");
        text = text.replace("X", "Х");
        return text;
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
