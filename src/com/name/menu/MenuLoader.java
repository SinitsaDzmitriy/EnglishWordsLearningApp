package com.name.menu;

import com.name.menu.commands.NoCommand;
import com.name.menu.commands.OpenUserDictionaryCommand;

public class MenuLoader {
    public static void main(String[] args) {
        Menu mainMenu = new Menu("main menu");

        Menu userDictionaryMenu = new Menu("user dictionary menu");
        userDictionaryMenu.addCommand(new NoCommand());
        userDictionaryMenu.addCommand(new NoCommand());

        mainMenu.addCommand(new OpenUserDictionaryCommand("openuserdict", "Open \"User dictionary\"",userDictionaryMenu));
        mainMenu.addCommand(new NoCommand());

        mainMenu.interactWithUser();
    }
}
