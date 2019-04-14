package com.name.menu.commands;

abstract public class AbstractCommand {
    private String name;

    AbstractCommand() {}

    AbstractCommand(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    abstract public void execute();
}
