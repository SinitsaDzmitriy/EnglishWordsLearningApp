package com.name.database;

import com.name.SearchItem;
import com.name.entities.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatabaseBuilder {
    // Technical info:
    private final static int LONG_SIZE = 8;

    // System info:
    private static final int WORD_GROUP = 2;
    private static final int TRANSLATION_GROUP = 4;
    private static final int NUM_OF_CARD_FIELDS = 2;

    // Files:
    private static final File source = new File("Server\\source.txt");
    private static final File data = new File("Server\\FieldsOfCards.data");
    private static final File index = new File("Server\\Indexes.data");
    private static final File rusSearch = new File("Server\\RusSearch.data");
    private static final File engSearch = new File("Server\\EngSearch.data");

    public static void main(String[] args) {
        buildDatabase(convertTXT());
    }

    public static Card[] convertTXT() {
        ArrayList<Card> packOfCards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-16"))) {
            // Search substrings like:
            // id   1stWord {(2ndWord, 3rdWord)}   [transcription]   translation
            // {...} - optional part,
            // 1stWord has to be in English
            // translation has to be in Russian
            String regEx = "(\\p{Digit}+)\\p{Blank}+(\\p{Alpha}[\\p{Alpha}\\- ]*\\p{Alpha})\\p{Blank}+\\(?[\\p{Alpha}, ]*\\)?\\p{Blank}*(\\[.+])\\p{Blank}+([A-яё \\-]+)";
            String currentLine = reader.readLine();
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher;
            Card newCard;

            while (currentLine != null) {
                matcher = pattern.matcher(currentLine);
                if (matcher.find()) {
                    newCard = new Card();
                    newCard.setWord(matcher.group(WORD_GROUP));
                    newCard.setTranslation(matcher.group(TRANSLATION_GROUP));
                    packOfCards.add(newCard);
                }
                currentLine = reader.readLine();
            }

//            for (Card card : packOfCards) {
//                System.out.println(card.toString());
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Card[] cards = new Card[packOfCards.size()];
        packOfCards.toArray(cards);
        return cards;
    }

    public static void buildDatabase(Card[] cards) {
        if (data.exists()) { data.delete(); }
        if (index.exists()) { index.delete(); }
        if (rusSearch.exists()) { rusSearch.delete(); }
        if (engSearch.exists()) { engSearch.delete(); }

        try {
            data.createNewFile();
            index.createNewFile();
            rusSearch.createNewFile();
            engSearch.createNewFile();
        } catch (IOException e) {
            System.err.println("File creation exception");
        }

        try (RandomAccessFile dataStream = new RandomAccessFile(data, "rw");
             RandomAccessFile indexStream = new RandomAccessFile(index, "rw")) {

            List < SearchItem > rusList = Arrays.stream(cards).map(card -> new SearchItem(card.getTranslation()))
                    .sorted(Comparator.comparing(SearchItem::getText))
                    .distinct()
                    .collect(Collectors.toList());

            List<SearchItem> engList = Arrays.stream(cards).map(card -> new SearchItem(card.getWord()))
                    .sorted(Comparator.comparing(SearchItem::getText))
                    .distinct()
                    .collect(Collectors.toList());

            for (int i = cards.length - 1; i >= 0; --i) {
                dataStream.write(cards[i].getWord().getBytes());
                indexStream.writeLong(dataStream.getFilePointer());

                dataStream.write(cards[i].getTranslation().getBytes());
                indexStream.writeLong(dataStream.getFilePointer());

                for (SearchItem item : rusList) {
                    if (item.getText() == cards[i].getTranslation()) {
                        item.addCardId(i);
                        break;
                    }
                }

                for (SearchItem item : engList) {
                    if (item.getText() == cards[i].getWord()) {
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
        }
    }
}
