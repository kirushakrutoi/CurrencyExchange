package ru.kirill.CurrencyExchange.dao;

import ru.kirill.CurrencyExchange.dao.interfaces.Dao;
import ru.kirill.CurrencyExchange.model.Currency;
import ru.kirill.CurrencyExchange.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<Currency> {

    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public List<Currency> index() throws SQLException {
        List<Currency> currencies = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM currencies";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            currencies.add(getCurrency(resultSet));
        }

        return currencies;
    }

    @Override
    public void save(Currency currency) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO currencies(code, fullname, sign) " +
                "VALUES(?, ?, ?)");

        preparedStatement.setString(1, currency.getCode());
        preparedStatement.setString(2, currency.getFullName());
        preparedStatement.setString(3, currency.getSign());

        preparedStatement.executeUpdate();
    }

    @Override
    public Optional<Currency> show(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies WHERE id=?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return Optional.of(getCurrency(resultSet));
        } catch (SQLException e){
            return Optional.empty();
        }
    }


    public Optional<Currency> findByCode(String code) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies WHERE code=?");
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return Optional.of(getCurrency(resultSet));
        } catch (SQLException e){
            return Optional.empty();
        }

    }

    private Currency getCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();

        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setFullName(resultSet.getString("fullname"));
        currency.setSign(resultSet.getString("sign"));

        return currency;
    }
}
