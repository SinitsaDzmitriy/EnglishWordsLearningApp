package com.name.client;

import com.name.entities.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientFilesDataProvider implements IClientDataProvider {
    public final static List<String>
    private static File data;
    private static File index;
    private static File rusSearch;
    private static File engSearch;

    public ClientFilesDataProvider(File first, File second, File third, File forth) {
        data = first;
        index = second;
        rusSearch = third;
        engSearch = forth;
    }

    @Override
    public void pushFilesWithData(List<File> files) {
        //
    }

    @Override
    public Card[] find(String text) {

        return new Card[0];
    }
}
