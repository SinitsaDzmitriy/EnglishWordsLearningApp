package com.name.database;

import com.name.entities.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseBuilder {
    private static final File source = new File("Server\\source.txt");
    private static final int WORD_GROUP = 2;
    private static final int TRANSLATION_GROUP = 4;

    public static void main(String[] args) {
        convertTXT();
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
                if(matcher.find()) {
                    newCard = new Card();
                    newCard.setWord(matcher.group(WORD_GROUP));
                    newCard.setTranslation(matcher.group(TRANSLATION_GROUP));
                    packOfCards.add(newCard);
                }
                currentLine = reader.readLine();
            }

            for (Card card : packOfCards) {
                System.out.println(card.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Card[] cards = new Card[packOfCards.size()];
        packOfCards.toArray(cards);
        return cards;
    }
}
