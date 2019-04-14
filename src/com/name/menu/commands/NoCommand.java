package com.name.menu.commands;

public class NoCommand extends AbstractCommand {
    public NoCommand() { super("I do nothing"); }

    @Override
    public void execute() {}
}
