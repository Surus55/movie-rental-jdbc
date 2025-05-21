package hu.vantus.movierental;

import hu.vantus.movierental.persistence.DBInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mariadb://localhost:3306/movie_rental"; // saját DB-ed neve
        String username = "root";  // vagy más, ha nem root a felhasználód
        String password = "";      // írd be a jelszót, ha van

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            DBInitializer.initializeDatabase(connection);
            System.out.println("✅ Az adatbázis inicializálása sikeres volt!");
        } catch (SQLException e) {
            System.err.println("❌ Hiba történt az adatbáziskapcsolat során: " + e.getMessage());
        }
    }
}
