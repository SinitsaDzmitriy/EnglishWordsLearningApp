package com.name.client;

import com.name.entities.Card;

import java.io.File;
import java.io.FileNotFoundException;

abstract public class AClientFilesDataProvider implements IClientDataProvider {
    protected File[] files;

    public String[] getNamesOfNeededFiles() { return null; }
    public void pushFile(File file) { }

    abstract public Card[] find(String text) throws FileNotFoundException;
}
