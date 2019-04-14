package com.name;

import java.util.ArrayList;

public class SearchItem {
    private String text;
    private ArrayList<Integer> ides;

    SearchItem(String text) {
        this.text = text;
        this.ides = new ArrayList<>();
    }

    public String getText() { return text; }

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

    void addId(int id){
        ides.add(id);
    }

    public String toString() {
        return text + " " + ides.toString();
    }
}
