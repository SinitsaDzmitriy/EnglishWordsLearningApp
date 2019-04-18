package com.name.client.dataprovider;

import com.name.entities.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

abstract public class AClientFilesDataProvider implements IClientDataProvider {
    protected File[] files;

    public ArrayList<String> getNamesOfNeededFiles() {
        ArrayList<String> namesOfNeededFiles = new ArrayList<>();
        for(File file: files){
            if(!file.exists()){
                namesOfNeededFiles.add(file.getName());
            }
        }
        return namesOfNeededFiles;
    }

    public void pushFile(File newFile) {
        if(newFile.exists()) {
            String newFileName = newFile.getName();
            for (File currentFile : files) {
                if (currentFile.getName().equals(newFileName)) {
                    if (!currentFile.exists()) {
                        // Move physical file associated with object newFile
                        // in the directory determined in currentFile object
                        newFile.renameTo(currentFile);
                    }
                    break;
                }
            }
        }
    }

    abstract public Card[] find(String text) throws FileNotFoundException;
}
