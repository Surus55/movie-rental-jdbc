package hu.vantus.movierental.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initializeDatabase(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS movies (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "title VARCHAR(255), " +
                    "director VARCHAR(255), " +
                    "release_year INT, " +
                    "genre VARCHAR(100), " +
                    "rental_price_per_day DOUBLE, " +
                    "available BOOLEAN, " +
                    "duration_minutes INT, " +
                    "age_rating VARCHAR(10)" +
                    ")";
            stmt.execute(sql);
        }
    }
}
