package com.name.server.database;

import com.name.common.entities.Card;

public interface IDataProvider {
    Card[] find(String text);
}
