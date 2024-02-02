package ru.kirill.CurrencyExchange.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> index() throws SQLException;

    void save(T t) throws SQLException;

    Optional<T> show(int id) throws SQLException;
}
