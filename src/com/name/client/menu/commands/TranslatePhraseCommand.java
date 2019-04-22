package com.name.client.menu.commands;

import com.name.client.dataprovider.IClientDataProvider;
import com.name.common.entities.Card;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TranslatePhraseCommand extends ACommand {
    private final IClientDataProvider DATA_PROVIDER;
    private final Scanner KEYBOARD_INPUT;

    public TranslatePhraseCommand(String commandName, IClientDataProvider dataProvider, Scanner keyboardInput) {
        super(commandName);
        DATA_PROVIDER = dataProvider;
        KEYBOARD_INPUT = keyboardInput;
    }

    @Override
    public void execute() {
        try {
            if (DATA_PROVIDER != null && KEYBOARD_INPUT != null) {
                System.out.print("Enter a phrase or word you want to translate: ");
                Card[] cards = DATA_PROVIDER.find(KEYBOARD_INPUT.nextLine());

                if(cards != null) {
                    for(Card card: cards) {
                        System.out.println(card);
                    }
                } else {
                    System.out.println("The phrase wasn't found");
                }
            } else {
                // TODO
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage() + " file absences in the Data Storage.");
        } catch (IOException e) {
            System.err.println("IOException: phrase searching failure.\nNo further correct work is possible.");
            System.exit(1);
        }
    }
}
