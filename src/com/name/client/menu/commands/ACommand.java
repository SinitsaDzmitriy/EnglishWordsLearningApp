package com.name.client.menu.commands;

abstract public class ACommand {
    public final String NAME;

    ACommand(String name) {
        NAME = name;
    }

    abstract public void execute();
}
