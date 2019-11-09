import org.glassfish.grizzly.streams.Stream;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        //-Djava.net.useSystemProxies=true;
        //System.setProperty("java.net.useSystemProxies", "true");
/*
        System.getProperties().put( "proxySet", "true" );
        System.getProperties().put( "socksProxyHost", "127.0.0.1" );
        System.getProperties().put( "socksProxyPort", "9150" );
*/
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) { // что бот будет возвращать на наши сообщения
        SendMessage sendMessage = new SendMessage(); // инициализируем сообщение
        sendMessage.enableMarkdown(true); // включить возможность разметки

        sendMessage.setChatId(message.getChatId().toString()); // устанавливаем ID чата, что бы знать кому отвечать
        sendMessage.setReplyToMessageId(message.getMessageId()); // на какое именно сообщение мы отвечаем
        sendMessage.setText(text); // устанавливаем этому сообщению текст text(тот который мы отправляли в этот метод)
        try { // непосредственная отправка сообщения
            setButtons(sendMessage); // метод добавляющий клавиатуру и привязывающий ее к сообщению
            execute(sendMessage); // использую execute(sendMessage) (в видео был использован sendMessage(sendMessage), это метод устаревший, но тоже рабочий)
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    public void onUpdateReceived(Update update) { // получить сообщение через обновление (в данном случае через Long Pulling, бывают еще через Webhook)
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) { // что спрашивают и что отвечаем
                case "/help": // служебное сообщение
                    sendMsg(message, "Чем могу помочь, мой Господин?");
                    break;
                case "/setting": // служебное сообщение
                    sendMsg(message, "Что будем настраивать, мой Тысяча чертей?");
                    break;

                default: // ответ по умолчанию (т.е. ответ на любой другой вопрос)
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model)); // тут отправляем сообщение с погодой, погоду узнаем методом getWeather
                    } catch (IOException e) {
                        sendMsg(message, "Упс, такой город не нахожу я от чего то, сорян..."); // если ошибка, то отправляем сообщение
                    }
            }
        }
    }

    public void setButtons(SendMessage sendMessage) { // метод который определяет клавиатуру под текстовой панелью
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // создаем (инициализируем клавиатуру
        sendMessage.setReplyMarkup(replyKeyboardMarkup); // создаем разметку и передаем ее в replyKeyboardMarkup
        replyKeyboardMarkup.setSelective(true); // параметр определяющий, что клава доступна для всех пользовоталей
        replyKeyboardMarkup.setResizeKeyboard(true); // автоматом подгонять клаву под размеры
        replyKeyboardMarkup.setOneTimeKeyboard(false); // параметры определяющий, что не надо скрывать клавиатуру после использования
        List<KeyboardRow> keyboardRowList = new ArrayList<>(); // создаем лист кнопок
        KeyboardRow keyboardFirstRow = new KeyboardRow(); // создаем первый ряд кнопок

        keyboardFirstRow.add(new KeyboardButton("/help")); // добавляем кнопку "/help"
        keyboardFirstRow.add(new KeyboardButton("/setting")); // добавляем кнопку "/setting"

        keyboardRowList.add(keyboardFirstRow); // добавляем этот ряд кнопок в лист keyboardRowList
        replyKeyboardMarkup.setKeyboard(keyboardRowList); // устанавливаем этот лист с кнопками на клавиатуре

    }


    public String getBotUsername() {
        return "SonyaKhmuraMyBot";
    }

    public String getBotToken() {
        return "957015293:AAEvF1aPPC3b_sG_yLBOyL-Vp7rZ7Vy1Ks4";
    }
}
