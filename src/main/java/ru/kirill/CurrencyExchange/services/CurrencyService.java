package ru.kirill.CurrencyExchange.services;

import ru.kirill.CurrencyExchange.model.Currency;
import ru.kirill.CurrencyExchange.dao.CurrencyDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<Currency> index() throws SQLException {
        return currencyDao.index();
    }

    public Optional<Currency> show(int id) {
        return currencyDao.show(id);
    }

    public void save(Currency currency) throws SQLException {
        currencyDao.save(currency);
    }

    public Optional<Currency> findByCode(String code) {
        return currencyDao.findByCode(code);
    }
}
