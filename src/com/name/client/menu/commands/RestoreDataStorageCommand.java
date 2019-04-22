package com.name.client.menu.commands;

import com.name.client.Client;

public class RestoreDataStorageCommand extends ACommand {
    private static RestoreDataStorageCommand instance;

    private RestoreDataStorageCommand() {
        super("Restore local Data Storage");
    }

    public static RestoreDataStorageCommand getInstance() {
        if (instance == null) {
            instance = new RestoreDataStorageCommand();
        }
        return instance;
    }

    @Override
    public void execute() {
        Client.checkDataIntegrity();
    }
}
