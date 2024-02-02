package ru.kirill.CurrencyExchange.services;

import ru.kirill.CurrencyExchange.dao.ExchangeRateDao;
import ru.kirill.CurrencyExchange.model.ExchangeRate;
import ru.kirill.CurrencyExchange.model.DTO.ExchangeResp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyService currencyService = new CurrencyService();

    public List<ExchangeRate> index() throws SQLException {
        return exchangeRateDao.index();
    }

    public Optional<ExchangeRate> show(int id)  {
        return exchangeRateDao.show(id);
    }

    public void save(ExchangeRate exchangeRate) throws SQLException {
        exchangeRateDao.save(exchangeRate);
    }

    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
        return exchangeRateDao.findByCodes(baseCode, targetCode);
    }

    public Optional<ExchangeResp> convert(String baseCode, String targetCode, BigDecimal amount) {
        BigDecimal rate = getRate(baseCode, targetCode);

        if(rate != null){
            return Optional.of(
                    new ExchangeResp(
                            currencyService.findByCode(baseCode).get(),
                            currencyService.findByCode(targetCode).get(),
                            rate,
                            amount
                    )
            );
        }

        else return Optional.empty();
    }

    private BigDecimal getRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> exchangeRate;

        if((exchangeRate = findByCodes(baseCode, targetCode)).isPresent())
            return exchangeRate.get().getRate();

        if((exchangeRate = findByCodes(targetCode, baseCode)).isPresent())
            return new BigDecimal(1).divide(exchangeRate.get().getRate(), 2, RoundingMode.HALF_UP);

        Optional<ExchangeRate> USDToBase = findByCodes("USD", baseCode);
        Optional<ExchangeRate> USDToTarget = findByCodes("USD", targetCode);

        if(USDToBase.isPresent() && USDToTarget.isPresent())
            return USDToTarget.get().getRate().divide(USDToBase.get().getRate(), 2, RoundingMode.HALF_UP);

        return null;
    }
}
