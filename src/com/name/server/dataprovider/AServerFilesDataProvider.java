package com.name.server.dataprovider;

import java.io.File;

abstract public class AServerFilesDataProvider implements IServerDataProvider {
    protected File[] files;

    @Override
    abstract public File getFileByName(String name);
}
