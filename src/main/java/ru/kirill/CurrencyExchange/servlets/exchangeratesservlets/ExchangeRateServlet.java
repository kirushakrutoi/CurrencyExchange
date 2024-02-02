package ru.kirill.CurrencyExchange.servlets.exchangeratesservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kirill.CurrencyExchange.model.DTO.ErrorResp;
import ru.kirill.CurrencyExchange.model.ExchangeRate;
import ru.kirill.CurrencyExchange.services.ExchangeRateService;
import ru.kirill.CurrencyExchange.util.Validation;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/exchangerate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String param = pathInfo.split("/")[1];

        if(!Validation.isValidExchangeRateCurrencyParam(param)){
            objectMapper.writeValue(resp.getWriter(), new ErrorResp("Отсутсвует один или более коды валютных пар"));
            resp.setStatus(400);
            return;
        }

        String baseCode = param.substring(0, 3);
        String targetCode = param.substring(3);
        Optional<ExchangeRate> exchangeRate = exchangeRateService.findByCodes(baseCode, targetCode);

        if(exchangeRate.isPresent()) {
            objectMapper.writeValue(resp.getWriter(), exchangeRate.get());
            resp.setStatus(200);
            return;
        }

        objectMapper.writeValue(resp.getWriter(), new ErrorResp("Такой валютной пары не сущесвтует"));
        resp.setStatus(404);

    }
}
