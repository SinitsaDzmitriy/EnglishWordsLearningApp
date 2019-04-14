package com.name.entities;

import java.io.Serializable;

public class Card implements Serializable {
    private String word;
    private String translation;
    public Card(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }
    public String getTranslation() {
        return translation;
    }
    public int getWordLength() {
        return word.length();
    }
    public int getTranslationLength() {
        return translation.length();
    }
    public String toString() {
        return word + " - " + translation;
    }
}
