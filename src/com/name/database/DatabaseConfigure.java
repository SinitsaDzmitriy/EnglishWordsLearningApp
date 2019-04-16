package com.name.database;

import com.name.common.SearchItem;
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

    private DatabaseConfigure() {
    }

    //==================================================================================================================
    public static void buildDatabase(Card[] cards) {
        if (!data.exists() && !index.exists()) {
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
                        .sorted(Comparator.comparing(SearchItem::getPhrase))
                        .distinct()
                        .collect(Collectors.toList());

                List<SearchItem> engList = Arrays.stream(cards).map(card -> new SearchItem(card.getWord()))
                        .sorted(Comparator.comparing(SearchItem::getPhrase))
                        .distinct()
                        .collect(Collectors.toList());

                for (int i = cards.length - 1; i >= 0; --i) {
                    dataStream.write(cards[i].getWord().getBytes());
                    System.out.print(dataStream.getFilePointer() + " ");
                    indexStream.writeLong(dataStream.getFilePointer());

                    dataStream.write(cards[i].getTranslation().getBytes());
                    System.out.println(dataStream.getFilePointer());
                    indexStream.writeLong(dataStream.getFilePointer());

                    for (SearchItem item : rusList) {
                        if (item.getPhrase() == cards[i].getTranslation()) {
                            item.addCardId(i);
                            break;
                        }
                    }

                    for (SearchItem item : engList) {
                        if (item.getPhrase() == cards[i].getWord()) {
                            item.addCardId(i);
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
                    if (dataStream != null) {
                        dataStream.close();
                    }
                    if (indexStream != null) {
                        indexStream.close();
                    }
                } catch (IOException e) {
                    System.err.println("Error closing files");
                }
            }
        }
    }

//    //Future | params: String word or translation, returns: Card[] that matches with word or phrase
//    public static Card read(int entryNo) throws IOException{
//        Card card = null;
//
//        if (data.exists() && index.exists()) {
//            try (RandomAccessFile dataStream = new RandomAccessFile(data, "r");
//                 RandomAccessFile indexStream = new RandomAccessFile(index, "r")) {
//                long pos = (entryNo == 0) ? 0 : LONG_SIZE * (NUM_OF_FIELDS * entryNo - 1);
//                indexStream.seek(pos);
//                long[] labels = new long[NUM_OF_FIELDS + 1];
//
//                for (int i = 0; i <= NUM_OF_FIELDS; ++i) {
//                    labels[i] = indexStream.readLong();
//                }
//
//                // TODO заполнение массива fields в цикле for
//
//                int wordLength = (int) (labels[1] - labels[0]);
//                int translationLength = (int) (labels[2] - labels[1]);
//
//                byte[] wordInBytes = new byte[wordLength];
//                byte[] translationInBytes = new byte[translationLength];
//
//                dataStream.seek(labels[0]);
//
//                dataStream.read(wordInBytes);
//                dataStream.read(translationInBytes);
//
//                card = new Card(new String(wordInBytes),
//                        new String(translationInBytes));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return card;
//        } else {
//            throw new IOException("One of the database files wasn't found");
//        }
//    }
//
//    public static Card read(int entryNo) throws IOException{
//        Card card = null;
//
//        if (data.exists() && index.exists()) {
//            try (RandomAccessFile dataStream = new RandomAccessFile(data, "r");
//                 RandomAccessFile indexStream = new RandomAccessFile(index, "r")) {
//                long pos = (entryNo == 0) ? 0 : LONG_SIZE * (NUM_OF_CARD_FIELDS * entryNo - 1);
//                indexStream.seek(pos);
//                long[] labels = new long[NUM_OF_CARD_FIELDS + 1];
//
//                for (int i = 0; i <= NUM_OF_CARD_FIELDS; ++i) {
//                    labels[i] = indexStream.readLong();
//                }
//
//                // TODO заполнение массива fields в цикле for
//
//                int wordLength = (int) (labels[1] - labels[0]);
//                int translationLength = (int) (labels[2] - labels[1]);
//
//                byte[] wordInBytes = new byte[wordLength];
//                byte[] translationInBytes = new byte[translationLength];
//
//                dataStream.seek(labels[0]);
//
//                dataStream.read(wordInBytes);
//                dataStream.read(translationInBytes);
//
//                card = new Card(new String(wordInBytes),
//                        new String(translationInBytes));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return card;
//        } else {
//            throw new IOException("One of the database files wasn't found");
//        }
//    }
//
////    // Search in English (How to recognize language in Java?)
////    public static Card[] find(String text){
////        // TODO add language recognition
////        // TODO exception handling
////        try {
////            ArrayList<SearchItem> searchList = new ArrayList<>();
////            ObjectInputStream searchStream = new ObjectInputStream(new FileInputStream((engSearch)));
////            searchList = (ArrayList<SearchItem>) searchStream.readObject();
////            int index = -1;
////            for (int i = 0; i < searchList.size(); ++i) {
////                if(searchList.get(i).getPhrase().equals(text)){
////                    index = i;
////                    break;
////                }
////            }
////
////            if (index == -1) {
////                System.out.println("Word no found");
////            } else {
////                for (int id : searchList.get(index).getIdes()) {
////                    System.out.println(read(id).toString());
////                }
////            }
////
////            searchStream.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
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
// ==================================================================================================================