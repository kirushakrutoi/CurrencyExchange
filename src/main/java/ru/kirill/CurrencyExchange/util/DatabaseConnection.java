package ru.kirill.CurrencyExchange.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Connection connection;

    static {
        try (InputStream stream = DatabaseConnection.class.getClassLoader().getResourceAsStream("\\database.properties")) {
            Properties databaseProperties = new Properties();
            databaseProperties.load(stream);

            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(databaseProperties.getProperty("url"),
                    databaseProperties.getProperty("username_value"),
                    databaseProperties.getProperty("password"));

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DatabaseConnection(){

    }

    public static Connection getConnection(){
        return connection;
    }
}
