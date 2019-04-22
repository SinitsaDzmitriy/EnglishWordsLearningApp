package com.name;

import com.name.server.database.DatabaseBuilder;

import java.io.*;
import java.util.Scanner;

public class Test {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        DatabaseBuilder.buildDatabase();
    }
}
