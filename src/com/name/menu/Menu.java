package com.name.menu;

import com.name.menu.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    private String headline;
    private ArrayList<AbstractCommand> commands;
    private Scanner scan = new Scanner(System.in);

    public Menu(String headline) {
        this.headline = headline;
        this.commands = new ArrayList<>();
    }

    public void addCommand(AbstractCommand command) {
        if(command != null) {
            commands.add(command);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void interactWithUser() {
        int counter = 1;
        System.out.println("======= " + headline + " =======");
        for(AbstractCommand command: commands) {
            System.out.println(counter + ". " + command.getName());
            ++counter;
        }

        System.out.print("Your choice: ");

        int itemNo;
        String itemNoString;

        do{
            itemNoString = scan.nextLine();
            if (Pattern.matches("\\p{Digit}+\\p{Blank}*", itemNoString)) {
                itemNo = Integer.parseInt(itemNoString.trim());
                if (itemNo > 0 && itemNo <= commands.size()) {
                    break;
                }
            }
            System.out.println("Incorrect input");
            System.out.print("Next try: ");
        } while (true);

        execute(itemNo);
    }

    public void execute(int itemNo) {
        commands.get(itemNo - 1).execute();
    }
}
