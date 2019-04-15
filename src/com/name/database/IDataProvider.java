package com.name.database;

import com.name.entities.Card;

public interface IDataProvider {
    Card[] find(String text);
}
