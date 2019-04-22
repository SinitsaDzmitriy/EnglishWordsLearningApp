package com.name.database;

import com.name.common.entities.Card;

public interface IDataProvider {
    Card[] find(String text);
}
