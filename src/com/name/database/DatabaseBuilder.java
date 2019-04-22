package com.name.database;

import com.name.common.entities.SearchItem;
import com.name.common.entities.Card;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatabaseBuilder {
    // System info:
    private static final int WORD_GROUP = 2;
    private static final int TRANSLATION_GROUP = 4;

    // Files:
    private static final File source = new File("server\\source.txt");
    private static final File data = new File("server\\fields_of_cards.data");
    private static final File index = new File("server\\index.data");
    private static final File rusSearch = new File("server\\rus_search.data");
    private static final File engSearch = new File("server\\eng_search.data");

    public static void buildDatabase() throws FileNotFoundException {
        try {
            writeCardArrayInFiles(convertTXT());
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    // [?] Нужно ли обрабатывать исключение FileNotFoundException, если существование файла проверяется методом .exists()?
    // [?] Это нормально возвращать null? Альтернатива new Card[0]
    // [?] Корректна ли обработка в стиле вывод сообщения через поток System.err в catch?

    /**
     * @return null if ...
     * @throws FileNotFoundException
     */
    private static Card[] convertTXT() throws FileNotFoundException {
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
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File Server\\source.txt doesn't exist");
        } catch (IOException e) {
            // TODO more info about exceptions
            System.err.println("IOException occurred ");
        }

        Card[] cards = new Card[packOfCards.size()];
        packOfCards.toArray(cards);
        return cards;
    }

    private static void writeCardArrayInFiles(Card[] cards) {
        if (data.exists()) {
            data.delete();
        }
        if (index.exists()) {
            index.delete();
        }
        if (rusSearch.exists()) {
            rusSearch.delete();
        }
        if (engSearch.exists()) {
            engSearch.delete();
        }

        try {
            data.createNewFile();
            index.createNewFile();
            rusSearch.createNewFile();
            engSearch.createNewFile();
        } catch (IOException e) {
            System.err.println("Creating database files exceptions");
        }

        try (RandomAccessFile dataStream = new RandomAccessFile(data, "rw");
             RandomAccessFile indexStream = new RandomAccessFile(index, "rw")) {

            List<SearchItem> rusList = Arrays.stream(cards).map(card -> new SearchItem(card.getTranslation()))
                    .sorted(Comparator.comparing(SearchItem::getPhrase))
                    .distinct()
                    .collect(Collectors.toList());

            List<SearchItem> engList = Arrays.stream(cards).map(card -> new SearchItem(card.getWord()))
                    .sorted(Comparator.comparing(SearchItem::getPhrase))
                    .distinct()
                    .collect(Collectors.toList());

            for (int i = 0; i < cards.length; ++i) {
                dataStream.write(cards[i].getWord().getBytes());
                indexStream.writeLong(dataStream.getFilePointer());

                dataStream.write(cards[i].getTranslation().getBytes());
                indexStream.writeLong(dataStream.getFilePointer());

                for (SearchItem item : rusList) {
                    if (item.getPhrase().equals(cards[i].getTranslation())) {
                        item.addCardId(i);
                        break;
                    }
                }

                for (SearchItem item : engList) {
                    if (item.getPhrase().equals(cards[i].getWord())) {
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

        } catch (FileNotFoundException e) {
            System.err.println("Database files were deleted or corrupted during their forming");
        } catch (IOException e) {
            System.err.println("IOException occurred");
        }
    }
}
