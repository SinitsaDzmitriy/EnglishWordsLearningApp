package com.name.server.dataprovider;

import java.io.File;
import java.util.List;

public interface IServerDataProvider {
    File getFileByName(String name);
}