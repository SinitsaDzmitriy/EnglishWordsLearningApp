package com.name.client;

import com.name.entities.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface IClientDataProvider {
    ArrayList<String> getNamesOfNeededFiles();
    void pushFile(File file);
    Card[] find(String text) throws FileNotFoundException;
}
