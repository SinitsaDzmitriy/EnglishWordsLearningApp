package com.name;

import com.name.client.dataprovider.ClientFilesDataProvider;
import com.name.common.entities.Card;

import java.io.*;
import java.util.Scanner;

public class Test {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("[Server]IOException: bla-bla-bla");
        try {
            ClientFilesDataProvider dataProvider = ClientFilesDataProvider.getInstance();
            String searchPhrase = scan.nextLine();
            Card[] cards = dataProvider.find(searchPhrase);
            for (Card card : cards) {
                System.out.println(card.toString());
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
