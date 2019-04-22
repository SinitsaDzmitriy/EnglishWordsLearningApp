package com.name.server.database;

import com.name.common.entities.Card;

import java.io.File;

public class FilesDataProvider implements IDataProvider {
    // 0: contains fields of cards written one by one
    // 1: contains positions of this fields as an array of longs
    // 2: contains ArrayList<SearchItem>, where written words (phrases) in Russian and indexes of associated Cards
    // 3: contains ArrayList<SearchItem>, where written words (phrases) in English and indexes of associated Cards
    private static File[] databaseFiles = {new File("Server\\FieldsOfCards.data"),
            new File("Server\\Indexes.data"),
            new File("Server\\RuSearch.data"),
            new File("Server\\EngSearch.data")};

    private FilesDataProvider() {}


    @Override
    public Card[] find(String text) {
        return new Card[0];
    }
}
