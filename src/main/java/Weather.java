import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {  // класс погода
    public static String getWeather(String message, Model model) throws IOException { // метод получения погоды
        // После appid= идет номер моего токиена 9ada15a775f87e3f9f678dee7780996c который я создал после регистрации на сайте
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=9ada15a775f87e3f9f678dee7780996c");

        Scanner in = new Scanner((InputStream) url.getContent()); // сканируем что передает нам url
        String result = ""; // тут будет результат
        while (in.hasNext()) { // перебираем пока есть что перебирать в отсканированном тексте
            result += in.nextLine(); // добавляем в result все что прочитано сканером
        }
// получаем название города
        JSONObject object = new JSONObject(result); // создаем Джейсон объект (из того что прочитал сканер) и заносим в него текст из result
        model.setName(object.getString("name")); // при помощи getString() выдергиваем "name" из Джейсоновского объекта object, и заносим его в нашу model при помощи setName() (setName присваивает значение переменной "name")
// получаем температуру и влажность
        JSONObject main = object.getJSONObject("main");  // создаем второй Джейсоновский объект (маленький) из object, он автоматически найдется в поле "main"
        model.setTemp(main.getDouble("temp")); // из main выдергиваем температруру "temp" при помощи getDouble()
        model.setHumidity(main.getDouble("humidity")); // из main выдергиваем влажность "humidity" при помощи getDouble()
// получаем погоду и иконку
        JSONArray getArray = object.getJSONArray("weather"); // создаем Джейсоновский массив из object, он автоматически найдется в поле "weather"
        for (int i = 0; i < getArray.length(); i++) { // перебираем Джейсоновский массив
            JSONObject obj = getArray.getJSONObject(i); // создаем(инциализируем) объект который выдернули из массива getArray
            model.setIcon((String) obj.get("icon")); // из obj выдергиваем иконку "icon" и присваиваем ее model
            model.setMain((String) obj.get("main")); // из obj выдергиваем погоду "main" и присваиваем ее model
        }


        return "Город: " + model.getName() + "\n" +  // возвращаем название города и перенос строки
                "Температура: " + model.getTemp() + "C" + "\n" + //
                "Влажность: " + model.getHumidity() + "%" + "\n" +
                "Ожидается: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png"; // возвращаем иконку
    }

}
