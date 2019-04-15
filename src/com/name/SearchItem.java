package com.name;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchItem implements Serializable {
    private String text;
    private ArrayList<Integer> ides;

    public SearchItem(String text) {
        this.text = text;
        this.ides = new ArrayList<>();
    }

    public String getText() { return text; }

    public ArrayList<Integer> getIdes() { return ides; }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof SearchItem) {
            SearchItem anotherItem = (SearchItem)anObject;
            return text.equals(anotherItem.getText());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    public void addCardId(int id){
        ides.add(id);
    }

    public String toString() {
        return text + " " + ides.toString();
    }
}
