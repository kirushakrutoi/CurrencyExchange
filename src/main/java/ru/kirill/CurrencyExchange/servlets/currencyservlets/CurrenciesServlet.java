package ru.kirill.CurrencyExchange.servlets.currencyservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kirill.CurrencyExchange.model.DTO.ErrorResp;
import ru.kirill.CurrencyExchange.services.CurrencyService;
import ru.kirill.CurrencyExchange.model.Currency;
import ru.kirill.CurrencyExchange.util.Validation;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            objectMapper.writeValue(resp.getWriter(), currencyService.index());
            resp.setStatus(200);

        } catch (SQLException e) {
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Ошибка базы данных"));
            resp.setStatus(500);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String fullName = req.getParameter("fullName");
        String sign = req.getParameter("sign");

        if(!Validation.isValidCurrencyParam(code, fullName, sign)){
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Один или более параметров не введены"));
            resp.setStatus(400);
            return;
        }

        try {
            currencyService.save(new Currency(code, fullName, sign));
            resp.setStatus(200);

        } catch (SQLException e) {
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Такой код валюты уже существует"));
            resp.setStatus(409);
        }
    }
}
