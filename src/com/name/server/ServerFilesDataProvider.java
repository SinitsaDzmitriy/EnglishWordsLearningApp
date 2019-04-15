package com.name.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO абстрактный класс data provider (одиночка)
public class ServerFilesDataProvider implements IServerDataProvider{
    private static ServerFilesDataProvider instance;
    private ServerFilesDataProvider() {}
    public static ServerFilesDataProvider getInstance() {
        if
    }

    @Override
    public List<File> getFilesWithData() {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File("Server\\FieldsOfCards.data"));
        files.add(new File("Server\\Indexes.data"));
        files.add(new File("Server\\RuSearch.data"));
        files.add(new File("Server\\EngSearch.data"));
        return files;
    }
}
