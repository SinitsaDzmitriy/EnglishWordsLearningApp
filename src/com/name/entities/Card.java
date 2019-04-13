package com.name.entities;

import java.io.Serializable;

public class Card implements Serializable {
    private String word;
    private String translation;
    public Card(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }
}
