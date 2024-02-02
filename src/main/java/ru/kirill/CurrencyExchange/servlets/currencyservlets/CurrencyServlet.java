package ru.kirill.CurrencyExchange.servlets.currencyservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kirill.CurrencyExchange.model.Currency;
import ru.kirill.CurrencyExchange.model.DTO.ErrorResp;
import ru.kirill.CurrencyExchange.services.CurrencyService;
import ru.kirill.CurrencyExchange.util.Validation;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        String code = parts[1];

        if(!Validation.isValidCode(code)){
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Код валюты не введен"));
            resp.setStatus(409);
            return;
        }

        Optional<Currency> currency = currencyService.findByCode(code);

        if(currency.isPresent()){
            objectMapper.writeValue(resp.getWriter(), currency.get());
            resp.setStatus(200);
            return;
        }

        objectMapper.writeValue(resp.getWriter(), new ErrorResp("Валюта с таким кодом не найдена"));
        resp.setStatus(404);

    }
}
