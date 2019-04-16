package com.name.client;

import com.name.entities.Card;

import java.io.File;
import java.io.FileNotFoundException;

public interface IClientDataProvider {
    String[] getNamesOfNeededFiles();
    void pushFile(File file);
    Card[] find(String text) throws FileNotFoundException;
}
