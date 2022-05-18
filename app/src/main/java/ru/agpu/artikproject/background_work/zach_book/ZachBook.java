package ru.agpu.artikproject.background_work.zach_book;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.OnSwipeTouchListener;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.layout.MainActivity;

public class ZachBook  extends Thread {
    public static List<RecyclerViewItems> MARKS_LIST;
    public static ArrayList<String> SEMESTR_LIST = new ArrayList<>();
    final String USERNAME;
    final String PASSWORD;
    final Activity act;
    public static boolean IS_LOADED = false;
    public String FIO;

    /**
     * Данный класс обращается к ЭИОС посредством передачи логина и пароля и получает зачетную книжку студента
     * @param act Активити
     * @param USERNAME Логин пользователя
     * @param PASSWORD Пароль пользователя
     */
    public ZachBook(Activity act, String USERNAME, String PASSWORD)  throws IOException {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.act = act;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        try{// Проверяем переданные параметры, чтобы они не были пустыми
            if (USERNAME.equals("") || PASSWORD.equals(""))
                IS_LOADED = false; // Если параметры пустые - возвращаем false
            URL url = new URL("http://plany.agpu.net/api/tokenauth"); // Указываем ссылку на страницу авторизации
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Открываем поключение
            connection.setRequestMethod("POST"); // Устанавливаем тип запроса - POST
            connection.setDoOutput(true); // Устанавливаем ожидание данных для вывода
            connection.setRequestProperty("Content-Type", "application/json"); // Указываем тип необходимого контента

            // Добавляем наши логин и пароль в дату
            String data = "{\n  \"userName\": \"" + USERNAME + "\",\n  \"password\": \"" + PASSWORD + "\"}";
            // Переводим дату в байты
            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            // Запускаем прослушку на выходное подключение
            OutputStream stream = connection.getOutputStream();
            stream.write(out); // И отправляем дату
            if (connection.getResponseCode() == 200){ // Если запрос прошел
                InputStream inputStream = connection.getInputStream(); // Получаем информацию
                String text = new BufferedReader( // И переводим ее в текст
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining("\n"));
                // Далее извлекаем токен в переменную accessToken
                String accessToken = text.split("\"accessToken\":")[1].split(",")[0];
                if (accessToken.equals("null")) // Если токен пустой
                    IS_LOADED = false; // Возвращаем false
                else // Если токен не пустой, удаляем кавычки в начале и конце
                    accessToken = accessToken.replace("\"", "");

                // Отправляем новый запрос на тот же адрес
                url = new URL("http://plany.agpu.net/api/tokenauth");
                connection = (HttpURLConnection) url.openConnection(); // Открываем соединение
                connection.setRequestMethod("GET"); // Но теперь это будет GET - запрос
                connection.setRequestProperty("Authorization", "Bearer " + accessToken); // Устанавливаем параметры
                connection.setRequestProperty("Cookie", "authToken=" + accessToken); // С полученным токеном
                inputStream = connection.getInputStream(); // Получаем информацию
                text = new BufferedReader( // И переводим ее в текст
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining("\n"));
                // Извлекаем ФИО студента
                FIO = text.split("\"last_name\":\"")[1].split("\"")[0] + " " +
                        text.split("\"first_name\":\"")[1].split("\"")[0] + " " +
                        text.split("\"middle_name\":\"")[1].split("\"")[0];
                // Извлекаем ID студента
                String USER_ID = text.split("\"userID\":-")[1].split("\"")[0];

                // После получения ID студента, указываем новую ссылку - на зачетку с ID пользователя
                url = new URL("http://plany.agpu.net/Ved/ZachBooks.aspx?id=" + USER_ID);
                Document doc = Jsoup.connect(String.valueOf(url)).get(); // Парсим всю страницу
                Elements links = doc.getElementsByClass("dxgvDataRow_MaterialCompact"); // Выбираем необходимые строки
                List<RecyclerViewItems> list = new ArrayList<>();
                for (Element src : links) { // И проходим по этим строкам с оценками
                    String current_src = src.toString();

                    String[] splited = current_src.split("</a>")[0].split(">");
                    String SEMESTR = splited[splited.length-1]; // Находим семестр

                    splited = current_src.split("</a>")[1].split(">");
                    String DISTCIPLINA = splited[splited.length-1]; // Находим дисциплину

                    splited = current_src.split("</td>")[2].split(">");
                    String MARK = splited[splited.length-1]; // Находим оценку

                    splited = current_src.split("</td>")[3].split(">");
                    String PREPOD = splited[splited.length-1]; // Находим преподавателя

                    splited = current_src.split("</td>")[6].split(">");
                    String MARK_TYPE = splited[splited.length-1]; // Находим тип оценки (экз\зачет)

                    // Добавляем полученные элементы с список
                    list.add(new RecyclerViewItems(SEMESTR, DISTCIPLINA, MARK, PREPOD, MARK_TYPE));
                    if (!SEMESTR_LIST.contains(SEMESTR)) // Если семестра нет в списке
                        SEMESTR_LIST.add(SEMESTR); // То добавляем его
                }
                MARKS_LIST = list; // Обновляем глобальный список
                Collections.sort(SEMESTR_LIST); // Сортируем список семестров
                Collections.sort(MARKS_LIST, Comparator.comparing(RecyclerViewItems::getMainText)); // Сортируем список оценок по семестрам
                IS_LOADED = true; // Ставим переключатель в состояние true - данные успешно загружены
            }
            else // Если сервер ответил ошибкой или не ответил
                IS_LOADED = false; // Возвращаем false
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally { // После отправки запроса и получения результата запускаем функцию обновления
            IS_LOADED(); // Экрана пользователя
        }
    }

    /**
     * Данная функция обновляет информацию о зачетке
     */
    @SuppressLint("ClickableViewAccessibility")
    public void IS_LOADED(){
        act.runOnUiThread(() ->{ // Все действия выполняются в главном потоке
            TextView mainText = act.findViewById(R.id.zachetka_main_text); // Основной текст
            Button loginButton = act.findViewById(R.id.login_btn); // Кнопка авторизации
            if(ZachBook.IS_LOADED) { // Если данные были успешно загружены
                // Сохраняем логин и пароль пользователя, чтобы он не вводил их снова
                MySharedPreferences.put(act.getApplicationContext(), "EIOS_LOGIN", USERNAME);
                MySharedPreferences.put(act.getApplicationContext(), "EIOS_PASSWORD", PASSWORD);

                EditText ETLogin = act.findViewById(R.id.login); // Поле ввода логина
                EditText ETPassword = act.findViewById(R.id.password); // Поле ввода пароля
                RecyclerView recyclerView = act.findViewById(R.id.recyclerView); // Список с оценками

                recyclerView.setVisibility(View.VISIBLE); // Показываем список с оценками
                ETLogin.setVisibility(View.INVISIBLE); // Скрываем поле ввода логина
                ETPassword.setVisibility(View.INVISIBLE); // Скрываем поле ввода пароля
                // Устанавливаем полученные данные в адаптер
                recyclerView.setAdapter(new RecyclerViewAdapter(act, ZachBook.MARKS_LIST, RecyclerViewAdapter.IS_MARK_ADAPTER));
                String newMainText = FIO + ", " + act.getString(R.string.your_record_book) + ":";
                mainText.setText(newMainText); // Выводим новый основной текст
                loginButton.setVisibility(View.INVISIBLE); // Скрываем кнопку авторизации
                Spinner spinner = act.findViewById(R.id.spinner); // Спинер с семестрами
                TextView selection = act.findViewById(R.id.selection); // Текст спинера
                spinner.setVisibility(View.VISIBLE); // Показываем спинер
                selection.setVisibility(View.VISIBLE); // Показываем текст спинера
                // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
                ArrayAdapter<String> adapter = new ArrayAdapter(act, android.R.layout.simple_spinner_item, ZachBook.SEMESTR_LIST);
                // Определяем разметку для использования при выборе элемента
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter); // Применяем адаптер к элементу spinner
                // Прослушиваем выбор нового семестра в спинере
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String semestr = (String) adapterView.getItemAtPosition(i); // Получаем семестр
                        List<RecyclerViewItems> MARKS_LIST_SORTED = new ArrayList<>();
                        for (RecyclerViewItems item: ZachBook.MARKS_LIST) // И сортируем данные по семестру
                            if (item.getMainText().equals(semestr))
                                MARKS_LIST_SORTED.add(item);
                        Collections.sort(MARKS_LIST_SORTED, Comparator.comparing(RecyclerViewItems::getSubText2));
                        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(act, MARKS_LIST_SORTED, RecyclerViewAdapter.IS_MARK_ADAPTER);
                        // Устанавливаем новый адаптер
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView){}
                });
                // Слушатель свайпов зачетки
                recyclerView.setOnTouchListener(new OnSwipeTouchListener(act.getApplicationContext()) {
                    public void onSwipeLeft() {
                        recyclerView.startAnimation(MainActivity.animUehalVlPriehalSprava);
                        int count = spinner.getCount();
                        int current_id = spinner.getSelectedItemPosition();
                        if (current_id+1 > count-1)
                            current_id = 0;
                        else
                            current_id += 1;
                        spinner.setSelection(current_id);
                    }
                    public void onSwipeRight(){
                        recyclerView.startAnimation(MainActivity.animUehalVpPriehalSleva);
                        int count = spinner.getCount();
                        int current_id = spinner.getSelectedItemPosition();
                        if (current_id-1 < 0)
                            current_id = count-1;
                        else
                            current_id -= 1;
                        spinner.setSelection(current_id);
                    }
                });
            }
            else{ // Если данные не были получены
                mainText.setText(act.getString(R.string.Incorrect_data_entered)); // Выводим сообщение об ошибке
                loginButton.setClickable(true); // Возвращаем кликабельность кнопке
            }
            loginButton.startAnimation(MainActivity.animRotate_ok); // Завершаем анимацию
        });
    }
}
