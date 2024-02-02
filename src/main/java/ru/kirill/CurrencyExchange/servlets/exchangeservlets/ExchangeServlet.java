package ru.kirill.CurrencyExchange.servlets.exchangeservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kirill.CurrencyExchange.model.DTO.ErrorResp;
import ru.kirill.CurrencyExchange.model.DTO.ExchangeResp;
import ru.kirill.CurrencyExchange.services.ExchangeRateService;
import ru.kirill.CurrencyExchange.util.Validation;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        String stringAmount = req.getParameter("amount");

        if(!Validation.isValidExchangeParam(baseCode, targetCode, stringAmount)){
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Введены не все параметры"));
            resp.setStatus(400);
            return;
        }


        BigDecimal amount = new BigDecimal(stringAmount);
        Optional<ExchangeResp> exchangeResp;
        exchangeResp = exchangeRateService.convert(baseCode, targetCode, amount);

        if(exchangeResp.isPresent()) {
            objectMapper.writeValue(resp.getWriter(), exchangeResp.get());
            resp.setStatus(200);
            return;
        }

        objectMapper.writeValue(resp.getWriter(), new ErrorResp("Не удалось найти такую валюту"));
        resp.setStatus(404);

    }
}
