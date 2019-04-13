package com.name;

import com.name.entities.Card;

import java.io.*;

public class DatabaseConfigure {
    private DatabaseConfigure() {}
    public static File getDatabase() { return new File(""); }
    public boolean serializate(Card[] cards, File database) {
        ObjectOutputStream objectOutputStream = null;
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(database);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }
}

//======================================================================================================================
// Вопросы:
// 1. Как выбросить исключение с Сервера на Клиент
