package hu.vantus.movierental.service;

import hu.vantus.movierental.model.Customer;
import hu.vantus.movierental.model.Movie;
import hu.vantus.movierental.model.RentalEvent;
import hu.vantus.movierental.persistence.CustomerDao;
import hu.vantus.movierental.persistence.MovieDao;
import hu.vantus.movierental.persistence.RentalEventDao;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTests {

    private Connection conn;
    private MovieDao movieDao;
    private CustomerDao customerDao;
    private RentalEventDao eventDao;
    private MovieService movieService;
    private RentalEventService rentalService;

    @BeforeAll
    void setupAll() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement s = conn.createStatement()) {
            s.execute("""
                CREATE TABLE customers (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  first_name VARCHAR(255),
                  last_name VARCHAR(255),
                  email VARCHAR(255),
                  phone_number VARCHAR(50),
                  id_card_number VARCHAR(50),
                  country_code CHAR(3)
                )
            """);
            s.execute("""
                CREATE TABLE movies (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  title VARCHAR(255),
                  director VARCHAR(255),
                  release_year INT,
                  genre VARCHAR(100),
                  rental_price_per_day DOUBLE,
                  available BOOLEAN,
                  duration_minutes INT,
                  age_rating VARCHAR(10)
                )
            """);
            s.execute("""
                CREATE TABLE rental_events (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  movie_id INT,
                  customer_id INT,
                  rental_date DATE,
                  return_date DATE,
                  total_cost DOUBLE,
                  is_closed BOOLEAN
                )
            """);
        }
        movieDao     = new MovieDao(conn);
        customerDao  = new CustomerDao(conn);
        eventDao     = new RentalEventDao(conn);
        movieService = new MovieService(movieDao);
        rentalService = new RentalEventService(eventDao, movieDao);
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.execute("DELETE FROM rental_events");
            s.execute("DELETE FROM movies");
            s.execute("DELETE FROM customers");
        }
    }

    @AfterAll
    void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    void testMovieServiceQueries() throws SQLException {
        movieDao.addMovie(new Movie(0, "A", "D1", 2001, "G1", 5.0, true, 90, "PG"));
        movieDao.addMovie(new Movie(0, "B", "D2", 2002, "G2", 8.0, true, 120, "R"));
        movieDao.addMovie(new Movie(0, "C", "D1", 2003, "G3", 3.0, true, 60, "G"));

        // duration between 80 and 130 -> only B
        List<Movie> byDuration = movieService.findByDurationRange(80, 130);
        assertEquals(1, byDuration.size());
        assertEquals("B", byDuration.get(0).getTitle());

        // max price 5.0 -> A and C
        List<Movie> byPrice = movieService.findByMaxPrice(5.0);
        assertEquals(2, byPrice.size());

        // specific title/director
        List<Movie> byTD = movieService.findByTitleAndDirector("C", "D1");
        assertEquals(1, byTD.size());
        assertEquals("C", byTD.get(0).getTitle());
    }

    @Test
    void testRentalEventServiceLifecycle() throws SQLException {
        // előkészítjük az ügyfelet és filmet
        customerDao.addCustomer(new Customer(0, "X", "Y", "x@y.com", "000", "IDX", "HU"));
        int custId = customerDao.getAllCustomers().get(0).getId();

        movieDao.addMovie(new Movie(0, "M1", "Dir", 2020, "G", 10.0, true, 100, "PG"));
        int movId = movieDao.getAllMovies().get(0).getId();

        // 1) Kölcsönzés
        rentalService.rentMovie(movId, custId, LocalDate.now());
        Movie afterRent = movieDao.getMovieById(movId);
        assertFalse(afterRent.isAvailable(), "A filmnek nem szabad elérhetőnek lennie kölcsönzés után");

        List<RentalEvent> events = eventDao.getAllRentalEvents();
        assertEquals(1, events.size());
        RentalEvent ev = events.get(0);
        assertFalse(ev.isClosed());

        // 2) Visszaadás
        rentalService.returnMovie(ev.getId(), LocalDate.now().plusDays(2), 20.0);
        Movie afterReturn = movieDao.getMovieById(movId);
        assertTrue(afterReturn.isAvailable(), "A filmnek elérhetőnek kell lennie visszaadás után");

        RentalEvent updatedEv = eventDao.getAllRentalEvents().get(0);
        assertTrue(updatedEv.isClosed());
        assertEquals(20.0, updatedEv.getTotalCost());
    }
}
