package com.name.server.dataprovider;

import java.io.File;

public class ServerFilesDataProvider extends AServerFilesDataProvider {
    private static final int NUM_OF_FILES = 4;

    private static final int FIELDS = 0;
    private static final int INDEX = 1;
    private static final int ENG_SEARCH = 2;
    private static final int RUS_SEARCH = 3;

    private final String DATA_STORAGE_DIR_NAME = "server";

    private static ServerFilesDataProvider instance;

    private ServerFilesDataProvider() {
        files = new File[NUM_OF_FILES];
        files[FIELDS] = new File(DATA_STORAGE_DIR_NAME + File.separator + "fields_of_cards.data");
        files[INDEX] = new File(DATA_STORAGE_DIR_NAME + File.separator + "index.data");
        files[ENG_SEARCH] = new File(DATA_STORAGE_DIR_NAME + File.separator + "eng_search.data");
        files[RUS_SEARCH] = new File(DATA_STORAGE_DIR_NAME + File.separator + "rus_search.data");
    }

    public static ServerFilesDataProvider getInstance() {
        if (instance == null) {
            instance = new ServerFilesDataProvider();
        }
        return instance;
    }

    @Override
    public File getFileByName(String targetFileName) {
        for (File currentFile : files) {
            if (currentFile.getName().equals(targetFileName)) {
                if (currentFile.exists()) {
                    return currentFile;
                }
            }
        }
        return null;
    }
}
