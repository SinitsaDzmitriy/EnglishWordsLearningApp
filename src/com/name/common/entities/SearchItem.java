package com.name.common.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchItem implements Serializable {
    private String phrase;
    private ArrayList<Integer> IDs;

    public SearchItem (String phrase) {
        this.phrase = phrase;
        this.IDs = new ArrayList<>();
    }

    public String getPhrase() { return phrase; }

    public ArrayList<Integer> getIDs() { return IDs; }

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

    public void addCardID(int ID){
        IDs.add(ID);
    }

    public String toString() {
        return phrase + " " + IDs.toString();
    }
}
