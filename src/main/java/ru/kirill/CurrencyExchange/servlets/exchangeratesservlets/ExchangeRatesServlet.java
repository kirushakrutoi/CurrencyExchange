package ru.kirill.CurrencyExchange.servlets.exchangeratesservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kirill.CurrencyExchange.model.Currency;
import ru.kirill.CurrencyExchange.model.DTO.ErrorResp;
import ru.kirill.CurrencyExchange.model.ExchangeRate;
import ru.kirill.CurrencyExchange.services.CurrencyService;
import ru.kirill.CurrencyExchange.services.ExchangeRateService;
import ru.kirill.CurrencyExchange.util.Validation;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(urlPatterns = "/exchangerates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            objectMapper.writeValue(resp.getWriter(), exchangeRateService.index());
            resp.setStatus(200);
        } catch (SQLException e) {
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Ошибка"));
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCode = req.getParameter("baseCurrencyCode");
        String targetCode = req.getParameter("targetCurrencyCode");
        String stringRate = req.getParameter("rate");

        if(!Validation.isValidExchangeRatesParam(baseCode, targetCode, stringRate)){
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Один или более параметров не введены"));
            resp.setStatus(400);
            return;
        }

        try {
            BigDecimal rate = new BigDecimal(stringRate);
            Optional<Currency> baseCurrency = currencyService.findByCode(baseCode);
            Optional<Currency> targetCurrency = currencyService.findByCode(targetCode);

            if(baseCurrency.isPresent() && targetCurrency.isPresent()) {
                ExchangeRate exchangeRate =
                        new ExchangeRate(
                                baseCurrency.get(),
                                targetCurrency.get(),
                                rate
                        );
                exchangeRateService.save(exchangeRate);
                resp.setStatus(200);
            }

            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Одной или более валют не сущетсвует"));
            resp.setStatus(404);

        } catch (SQLException e) {
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Такая валютная пара уже существует"));
            resp.setStatus(409);
        }

    }

}
