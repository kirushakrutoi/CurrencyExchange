package ru.kirill.CurrencyExchange.dao;

import ru.kirill.CurrencyExchange.model.Currency;
import ru.kirill.CurrencyExchange.model.ExchangeRate;
import ru.kirill.CurrencyExchange.dao.interfaces.Dao;
import ru.kirill.CurrencyExchange.util.DatabaseConnection;

import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements Dao<ExchangeRate> {

    private final Connection connection = DatabaseConnection.getConnection();
    private final String query = "SELECT er.id AS er_id," +
            "bc.id AS cur_baseid," +
            "bc.code AS cur_basecode," +
            "bc.fullname AS cur_basefullname," +
            "bc.sign AS cur_basesign," +
            "tc.id AS cur_targetid," +
            "tc.code AS cur_targetcode," +
            "tc.fullname AS cur_targetfullname," +
            "tc.sign AS cur_targetsign," +
            "er.basecurrencyid AS er_baseid," +
            "er.targetcurrencyid AS er_targetid," +
            "er.rate AS er_rate " +
            "FROM exchangerates er " +
            "JOIN currencies bc ON er.basecurrencyid = bc.id " +
            "JOIN currencies tc on er.targetcurrencyid = tc.id ";

    @Override
    public List<ExchangeRate> index() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            exchangeRates.add(getExchangeRate(resultSet));
        }

        return exchangeRates;
    }

    @Override
    public void save(ExchangeRate exchangeRate) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("INSERT INTO exchangerates(basecurrencyid, targetcurrencyid, rate) " +
                "VALUES(?, ?, ?)");

        preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId().getId());
        preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId().getId());
        preparedStatement.setBigDecimal(3, exchangeRate.getRate());

        preparedStatement.executeUpdate();
    }

    @Override
    public Optional<ExchangeRate> show(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query + " WHERE er.id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return Optional.ofNullable(getExchangeRate(resultSet));
        } catch (SQLException e){
            return Optional.empty();
        }

    }

    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query +
                    "WHERE(er.basecurrencyid = (SELECT id FROM currencies WHERE code = ?) AND " +
                    "er.targetcurrencyid = (SELECT id FROM currencies WHERE code = ?))");

            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return Optional.ofNullable(getExchangeRate(resultSet));
        } catch (SQLException e){
            return Optional.empty();
        }

    }

    private ExchangeRate getExchangeRate(ResultSet resultSet)  {
        ExchangeRate exchangeRate = new ExchangeRate();
        Currency baseCurrency = new Currency();
        Currency targetCurrency = new Currency();

        try {
            exchangeRate.setId(resultSet.getInt("er_id"));
            exchangeRate.setRate(resultSet.getBigDecimal("er_rate").setScale(2, RoundingMode.HALF_UP));
            baseCurrency.setId(resultSet.getInt("cur_baseid"));
            baseCurrency.setCode(resultSet.getString("cur_basecode"));
            baseCurrency.setFullName(resultSet.getString("cur_basefullname"));
            baseCurrency.setSign(resultSet.getString("cur_basesign"));
            targetCurrency.setId(resultSet.getInt("cur_targetid"));
            targetCurrency.setCode(resultSet.getString("cur_targetcode"));
            targetCurrency.setFullName(resultSet.getString("cur_targetfullname"));
            targetCurrency.setSign(resultSet.getString("cur_targetsign"));
        } catch (SQLException e){
            return null;
        }

        exchangeRate.setBaseCurrencyId(baseCurrency);
        exchangeRate.setTargetCurrencyId(targetCurrency);

        return exchangeRate;
    }

}
