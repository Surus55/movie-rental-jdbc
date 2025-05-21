package hu.vantus.movierental.persistence;

import hu.vantus.movierental.model.Customer;
import hu.vantus.movierental.model.Movie;
import hu.vantus.movierental.model.RentalEvent;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DaoTests {

    private Connection connection;
    private CustomerDao customerDao;
    private MovieDao movieDao;
    private RentalEventDao rentalEventDao;

    @BeforeAll
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/movie_rental", "root", "");
        customerDao = new CustomerDao(connection);
        movieDao = new MovieDao(connection);
        rentalEventDao = new RentalEventDao(connection);
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testCustomerDaoOperations() throws SQLException {
        // ... ugyanaz mint korábban ...
    }

    @Test
    void testMovieDaoOperations() throws SQLException {
        // ... ugyanaz mint korábban ...
    }

    @Test
    void testRentalEventDaoOperations() throws SQLException {
        // 1. Ügyfél és film létrehozása
        Customer customer = new Customer(0, "Temp", "User", "temp@example.com", "111", "IDTEMP", "HU");
        customerDao.addCustomer(customer);
        int customerId = customerDao.getAllCustomers()
                .get(customerDao.getAllCustomers().size() - 1)
                .getId();

        Movie movie = new Movie(0, "Temp Movie", "Someone", 2020, "Drama", 3.5, true, 100, "PG");
        movieDao.addMovie(movie);
        int movieId = movieDao.getAllMovies()
                .get(movieDao.getAllMovies().size() - 1)
                .getId();

        // 2. RentalEvent hozzáadása
        RentalEvent event = new RentalEvent(0, movieId, customerId, LocalDate.now(), null, 0.0, false);
        rentalEventDao.addRentalEvent(event);

        // 3. Ellenőrzés: getAllRentalEvents
        List<RentalEvent> events = rentalEventDao.getAllRentalEvents();
        assertFalse(events.isEmpty());
        RentalEvent fetched = events.get(events.size() - 1);
        assertFalse(fetched.isClosed());

        // 4. Frissítés: updateReturnDateAndClose
        LocalDate returnDate = LocalDate.now().plusDays(3);
        double cost = 10.5;
        rentalEventDao.updateReturnDateAndClose(fetched.getId(), returnDate, cost);

        // 5. Újra lekérdezés és ellenőrzés
        RentalEvent updated = rentalEventDao.getAllRentalEvents().stream()
                .filter(e -> e.getId() == fetched.getId())
                .findFirst()
                .orElse(null);
        assertNotNull(updated);
        assertTrue(updated.isClosed());
        assertEquals(cost, updated.getTotalCost());
        assertEquals(returnDate, updated.getReturnDate());

        // 6. Törlés és végső ellenőrzés
        rentalEventDao.deleteRentalEvent(updated.getId());
        boolean stillExists = rentalEventDao.getAllRentalEvents().stream()
                .anyMatch(e -> e.getId() == updated.getId());
        assertFalse(stillExists);

        // 7. Takarítás
        movieDao.deleteMovie(movieId);
        customerDao.deleteCustomer(customerId);
    }
}
