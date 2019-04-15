package com.name.client;

import com.name.entities.Card;

import java.io.File;
import java.util.List;

public interface IClientDataProvider {
    void pushFilesWithData(List<File> files);
    void pushFile(File file);
    Card[] find(String text);

}
