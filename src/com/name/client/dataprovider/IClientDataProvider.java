package com.name.client.dataprovider;

import com.name.common.entities.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface IClientDataProvider {
    ArrayList<String> getNamesOfNeededFiles();
    void pushFile(File file);
    Card[] find(String text) throws FileNotFoundException;
}
