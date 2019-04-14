package com.name.menu;

import com.name.menu.commands.NoCommand;

public class MenuLoader {
    public static void main(String[] args) {
        Menu mainMenu = new Menu("main menu");

        mainMenu.addCommand(new NoCommand());
        mainMenu.addCommand(new NoCommand());
        mainMenu.addCommand(new NoCommand());

        mainMenu.interactWithUser();
    }
}
