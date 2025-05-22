package hu.vantus.movierental.service;

import hu.vantus.movierental.model.Customer;
import hu.vantus.movierental.model.Movie;
import hu.vantus.movierental.model.RentalEvent;
import hu.vantus.movierental.repository.CustomerRepository;
import hu.vantus.movierental.repository.MovieRepository;
import hu.vantus.movierental.repository.RentalEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalEventRepository rentalEventRepository;
    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;

    public RentalService(RentalEventRepository rentalEventRepository,
                         CustomerRepository customerRepository,
                         MovieRepository movieRepository) {
        this.rentalEventRepository = rentalEventRepository;
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
    }

    public List<RentalEvent> getAllRentals() {
        return rentalEventRepository.findAll();
    }

    public RentalEvent rentMovie(Long customerId, Long movieId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<Movie> movieOpt = movieRepository.findById(movieId);

        if (customerOpt.isEmpty() || movieOpt.isEmpty()) {
            throw new RuntimeException("Customer or Movie not found");
        }

        Movie movie = movieOpt.get();
        if (!movie.isAvailable()) {
            throw new RuntimeException("Movie is not available for rent");
        }

        movie.setAvailable(false);
        movieRepository.save(movie);

        RentalEvent rental = new RentalEvent();
        rental.setCustomer(customerOpt.get());
        rental.setMovie(movie);
        rental.setRentalDate(LocalDate.now());
        rental.setReturned(false);

        return rentalEventRepository.save(rental);
    }

    public RentalEvent returnMovie(Long rentalId) {
        Optional<RentalEvent> rentalOpt = rentalEventRepository.findById(rentalId);

        if (rentalOpt.isEmpty()) {
            throw new RuntimeException("RentalEvent not found");
        }

        RentalEvent rental = rentalOpt.get();
        if (rental.isReturned()) {
            throw new RuntimeException("Movie has already been returned");
        }

        rental.setReturnDate(LocalDate.now());
        rental.setReturned(true);

        Movie movie = rental.getMovie();
        movie.setAvailable(true);
        movieRepository.save(movie);

        return rentalEventRepository.save(rental);
    }
}
