package com.name;

import com.name.entities.Card;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Test {
    private static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        Card[] cards = { new Card("Car", "Вагон"),
                new Card("Auto", "Машина"),
                new Card("Cart", "Вагон"),
                new Card("Car", "Автомобиль")
        };
        method(cards);
    }

    public static void method(Card[] cards) {
        List<SearchItem> rusList = Arrays.stream(cards).map(card -> new SearchItem(card.getTranslation()))
                .sorted(Comparator.comparing(SearchItem::getText))
                .distinct()
                .collect(Collectors.toList());

        List<SearchItem> engList = Arrays.stream(cards).map(card -> new SearchItem(card.getWord()))
                .sorted(Comparator.comparing(SearchItem::getText))
                .distinct()
                .collect(Collectors.toList());

        for(int i = cards.length - 1; i >= 0; --i) {
            for(SearchItem item: rusList) {
                if (item.getText() == cards[i].getTranslation()){
                    item.addId(i);
                    break;
                }
            }

            for(SearchItem item: engList) {
                if (item.getText() == cards[i].getWord()){
                    item.addId(i);
                    break;
                }
            }
        }

        for(SearchItem item: rusList) { System.out.println(item.toString()); }
        System.out.println();
        for(SearchItem item: engList) { System.out.println(item.toString()); }
    }

}
