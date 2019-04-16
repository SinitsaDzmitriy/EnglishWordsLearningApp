package com.name;

import com.name.client.ClientFilesDataProvider;
import com.name.database.DatabaseBuilder;
import com.name.database.FilesDataProvider;
import com.name.entities.Card;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Test {
    private static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        File file = new File("test" + File.separator + "test.txt");
        File newFile = new File("test.txt");

        System.out.println("file name: " + file.getName());
        System.out.println("new file name: " + newFile.getName());

        System.out.println();

        System.out.println("file exists: " + file.exists());
        System.out.println("new file exists: " + newFile.exists());

        System.out.println();

        if(file.getName().equals(newFile.getName())){
            if (!file.exists()) {
                newFile.renameTo(file);
            }
        }

        System.out.println("file name: " + file.getName());
        System.out.println("new file name: " + newFile.getName());

        System.out.println();

        System.out.println("file exists: " + file.exists());
        System.out.println("new file exists: " + newFile.exists());


//        try {
//            DatabaseBuilder.buildDatabase();
//            ClientFilesDataProvider dataProvider = ClientFilesDataProvider.getInstance();
//            String searchPhrase = scan.nextLine();
//            Card[] cards = dataProvider.find(searchPhrase);
//            for (Card card : cards) {
//                System.out.println(card.toString());
//            }
//        } catch(FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }



}
