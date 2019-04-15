package com.name.server;

import java.io.File;
import java.util.List;

public interface IServerDataProvider {
    List<File> getFilesWithData();
    File getFileByName(String name);
}