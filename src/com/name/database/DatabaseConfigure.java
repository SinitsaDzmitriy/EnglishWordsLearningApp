package com.name.database;

import com.name.entities.Card;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseConfigure {
    private final static int NUM_OF_FIELDS = 2;
    private final static int LONG_SIZE = 8;
    private static File data;
    private static File index;
    private static File rusSearch;
    private static File engSearch;

    static {
        data = new File("Server\\FieldsOfCards.data");
        index = new File("Server\\Indexes.data");
        rusSearch = new File("Server\\RuSearch.data");
        engSearch = new File("Server\\EngSearch.data");
    }

    private DatabaseConfigure() {}

    //==================================================================================================================
    public static void buildDatabase(Card[] cards) {
        if(!data.exists() && !index.exists()) {
            RandomAccessFile dataStream = null;
            RandomAccessFile indexStream = null;

            try {
                data.createNewFile();
                index.createNewFile();

                rusSearch.createNewFile();
                engSearch.createNewFile();

                dataStream = new RandomAccessFile(data, "rw");
                indexStream = new RandomAccessFile(index, "rw");

                List<SearchItem> rusList = Arrays.stream(cards).map(card -> new SearchItem(card.getTranslation()))
                        .sorted(Comparator.comparing(SearchItem::getText))
                        .distinct()
                        .collect(Collectors.toList());

                List<SearchItem> engList = Arrays.stream(cards).map(card -> new SearchItem(card.getWord()))
                        .sorted(Comparator.comparing(SearchItem::getText))
                        .distinct()
                        .collect(Collectors.toList());

                for(int i = cards.length - 1; i >= 0; --i) {
                    dataStream.write(cards[i].getWord().getBytes());
                    System.out.print(dataStream.getFilePointer() + " ");
                    indexStream.writeLong(dataStream.getFilePointer());

                    dataStream.write(cards[i].getTranslation().getBytes());
                    System.out.println(dataStream.getFilePointer());
                    indexStream.writeLong(dataStream.getFilePointer());

                    for(SearchItem item: rusList) {
                        if (item.getText() == cards[i].getTranslation()){
                            item.addId(i);
                            break;
                        }
                    }

                    for(SearchItem item: engList) {
                        if (item.getText() == cards[i].getWord()){
                            item.addId(i);
                            break;
                        }
                    }
                }

                ObjectOutputStream searchStream = new ObjectOutputStream(new FileOutputStream(rusSearch));
                searchStream.writeObject(rusList);
                searchStream.close();

                searchStream = new ObjectOutputStream(new FileOutputStream((engSearch)));
                searchStream.writeObject(engList);
                searchStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(dataStream != null) {
                        dataStream.close();
                    }
                    if(indexStream != null) {
                        indexStream.close();
                    }
                } catch(IOException e) {
                    System.err.println("Error closing files");
                }
            }
        }
    }
    //==================================================================================================================

    //Future | params: String word or translation, returns: Card[] that matches with word or phrase
    public static Card read(int entryNo) throws IOException {
            if(data.exists() && index.exists()) {
                // TODO для entryNo = 1 не сработает!!!
                RandomAccessFile dataStream = new RandomAccessFile(data, "r");
                RandomAccessFile indexStream = new RandomAccessFile(index, "r");


                long pos = LONG_SIZE * (NUM_OF_FIELDS * entryNo - 1);
                System.out.println("position = " + pos);
                System.out.println("file length = "  + " " + index.length());

                indexStream.seek(pos);

                long[] labels = new long[NUM_OF_FIELDS + 1];
                for(int i = 0; i <= NUM_OF_FIELDS; ++i) {
                    labels[i] = indexStream.readLong();
                }

                System.out.println("my labels: ");
                for(long label: labels) {
                    System.out.print(label + " ");
                }
                System.out.println();

                // TODO заполнение массива fields в цикле for

                dataStream.seek(labels[0]);

                int wordLength = (int) (labels[1] - labels[0]);
                int translationLength = (int) (labels[2] - labels[1]);

                System.out.println("word = " + wordLength);
                System.out.println("translation = " + translationLength);

                byte[] wordInBytes = new byte[wordLength];
                byte[] translationInBytes = new byte[translationLength];

                dataStream.seek(labels[0]);

                dataStream.read(wordInBytes);
                dataStream.read(translationInBytes);

                String word = new String(wordInBytes);

                Card card = new Card(word,
                                     new String(translationInBytes));
                dataStream.close();
                indexStream.close();
                return card;
            } else {
                throw new IOException("One of the database files wasn't found");
            }
    }


    public static File getDatabase() {
        return new File("");
    }
}

//======================================================================================================================
// Вопросы:
// 1. Существует ли класс, храня
//======================================================================================================================

//public static boolean serializate(Card[] cards, File database) {
//    ObjectOutputStream objectOutputStream = null;
//    FileOutputStream fileOutputStream = null;
//    try {
//        fileOutputStream = new FileOutputStream(database);
//        objectOutputStream = new ObjectOutputStream(fileOutputStream);
//        //object
//    } catch (IOException e) {
//        e.printStackTrace();
//    } finally {
//        if (fileOutputStream != null)
//            try {
//                fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//    }
//    return true;
//}