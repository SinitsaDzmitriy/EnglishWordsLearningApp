package com.name.client;

import com.name.entities.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientFilesDataProvider implements IClientDataProvider {
    List<File> files;

    @Override
    public void pushFilesWithData(List<File> files) {
    }

    @Override
    public Card[] find(String text) {

        return new Card[0];
    }
}
