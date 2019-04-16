package com.name;

import com.name.client.ClientFilesDataProvider;
import com.name.database.DatabaseBuilder;
import com.name.database.FilesDataProvider;
import com.name.entities.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {
    private static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
//        File testFile = new File("client" + File.separator + "index.data");
//
//        FileNotFoundException e = new FileNotFoundException(testFile.getName());
//        System.out.println(e.getMessage());

        try {
            DatabaseBuilder.buildDatabase();
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
