package hu.vantus.movierental.service;

import hu.vantus.movierental.model.Customer;
import hu.vantus.movierental.model.Movie;
import hu.vantus.movierental.model.RentalEvent;
import hu.vantus.movierental.repository.CustomerRepository;
import hu.vantus.movierental.repository.MovieRepository;
import hu.vantus.movierental.repository.RentalEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieRentalServiceTest {

    private MovieRepository movieRepository;
    private CustomerRepository customerRepository;
    private RentalEventRepository rentalEventRepository;

    private MovieService movieService;
    private CustomerService customerService;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        customerRepository = mock(CustomerRepository.class);
        rentalEventRepository = mock(RentalEventRepository.class);

        movieService = new MovieService(movieRepository);
        customerService = new CustomerService(customerRepository);
        rentalService = new RentalService(rentalEventRepository, customerRepository, movieRepository);
    }

    @Test
    void testFindAllMovies() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setReleaseDate(LocalDate.of(2010, 7, 16));
        movie.setAvailable(true);

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<Movie> movies = movieService.getAllMovies();
        assertEquals(1, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
        verify(movieRepository).findAll();
    }

    @Test
    void testSaveMovie() {
        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setGenre("Sci-Fi");
        movie.setReleaseDate(LocalDate.of(2014, 11, 7));
        movie.setAvailable(true);

        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie saved = movieService.saveMovie(movie);
        assertEquals("Interstellar", saved.getTitle());
        verify(movieRepository).save(movie);
    }

    @Test
    void testFindAllCustomers() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice Smith");
        customer.setEmail("alice@example.com");

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> customers = customerService.findAll();
        assertEquals(1, customers.size());
        assertEquals("Alice Smith", customers.get(0).getName());
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("Bob Johnson");
        customer.setEmail("bob@example.com");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer saved = customerService.save(customer);
        assertEquals("Bob Johnson", saved.getName());
        verify(customerRepository).save(customer);
    }

    @Test
    void testGetAllRentals() {
        RentalEvent rental = new RentalEvent();
        rental.setId(1L);
        rental.setRentalDate(LocalDate.now());
        rental.setReturned(false);

        when(rentalEventRepository.findAll()).thenReturn(List.of(rental));

        List<RentalEvent> rentals = rentalService.getAllRentals();
        assertEquals(1, rentals.size());
        assertFalse(rentals.get(0).isReturned());
    }

    @Test
    void testRentMovie() {
        Long customerId = 1L;
        Long movieId = 2L;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("Test Movie");
        movie.setAvailable(true);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(rentalEventRepository.save(any(RentalEvent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RentalEvent rental = rentalService.rentMovie(customerId, movieId);

        assertNotNull(rental.getRentalDate());
        assertEquals(customer, rental.getCustomer());
        assertEquals(movie, rental.getMovie());
        assertFalse(rental.isReturned());

        verify(movieRepository).save(movie);
        verify(rentalEventRepository).save(any(RentalEvent.class));
    }

    @Test
    void testReturnMovie() {
        Long rentalId = 1L;

        Movie movie = new Movie();
        movie.setId(2L);
        movie.setTitle("Test Movie");
        movie.setAvailable(false);

        RentalEvent rental = new RentalEvent();
        rental.setId(rentalId);
        rental.setMovie(movie);
        rental.setRentalDate(LocalDate.now().minusDays(3));
        rental.setReturned(false);

        when(rentalEventRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(rentalEventRepository.save(any(RentalEvent.class))).thenReturn(rental);

        RentalEvent returned = rentalService.returnMovie(rentalId);

        assertTrue(returned.isReturned());
        assertNotNull(returned.getReturnDate());
        assertTrue(returned.getMovie().isAvailable());

        verify(movieRepository).save(movie);
        verify(rentalEventRepository).save(rental);
    }
}
