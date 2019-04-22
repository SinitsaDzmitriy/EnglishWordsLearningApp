package com.name.client.menu;

import com.name.client.menu.commands.ACommand;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    private final String HEADLINE;
    private final Scanner KEYBOARD_INPUT;

    private ArrayList<ACommand> commands;

    public Menu(String headline, Scanner keyboardInput) {
        HEADLINE = headline;
        KEYBOARD_INPUT = keyboardInput;
        commands = new ArrayList<>();
    }

    public void addItem(ACommand command) {
        if(command != null) {
            commands.add(command);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void interactWithUser() {
        while(true){
            int counter = 1;
            System.out.println("======= " + HEADLINE + " =======");
            for(ACommand command: commands) {
                System.out.println(counter + ". " + command.NAME);
                ++counter;
            }

            System.out.println("Enter 0 to exit.");
            System.out.print("Your choice: ");

            int itemNum;
            String strItemNum;

            do{
                strItemNum = KEYBOARD_INPUT.nextLine();
                if (Pattern.matches("\\p{Blank}*\\p{Digit}+\\p{Blank}*", strItemNum)) {
                    itemNum = Integer.parseInt(strItemNum.trim());
                    if (itemNum >= 0 && itemNum <= commands.size()) {
                        break;
                    }
                }
                System.out.println("Incorrect input");
                System.out.print("Next try: ");
            } while (true);

            if(itemNum == 0) {
                System.out.println("Have a good day!");
                break;
            }
            execute(itemNum);
        }
    }

    private void execute(int itemNo) {
        commands.get(itemNo - 1).execute();
    }
}
