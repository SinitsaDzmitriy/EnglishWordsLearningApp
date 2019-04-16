package com.name.common;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchItem implements Serializable {
    private String phrase;
    private ArrayList<Integer> ides;

    public SearchItem (String phrase) {
        this.phrase = phrase;
        this.ides = new ArrayList<>();
    }

    public String getPhrase() { return phrase; }

    public ArrayList<Integer> getIdes() { return ides; }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof SearchItem) {
            SearchItem anotherItem = (SearchItem)anObject;
            return phrase.equals(anotherItem.getPhrase());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return phrase.hashCode();
    }

    public void addCardId(int id){
        ides.add(id);
    }

    public String toString() {
        return phrase + " " + ides.toString();
    }
}
