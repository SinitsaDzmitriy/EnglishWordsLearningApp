package com.name.menu.commands;

abstract public class AbstractCommand {
    private String name;
    private String description;

    AbstractCommand() {}

    AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    abstract public void execute();
}
