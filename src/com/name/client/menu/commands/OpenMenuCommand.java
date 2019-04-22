package com.name.client.menu.commands;

import com.name.client.menu.Menu;

public class OpenMenuCommand extends ACommand {
    private Menu menu;

    OpenMenuCommand(String commandName, Menu menu) {
        super(commandName);
        this.menu = menu;
    }

    @Override
    public void execute() {
        menu.interactWithUser();
    }
}
