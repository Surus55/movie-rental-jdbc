package hu.vantus.movierental.service;

import hu.vantus.movierental.model.Movie;
import hu.vantus.movierental.persistence.MovieDao;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    /** Összetett keresések **/

    // 1.) Duration range
    public List<Movie> findByDurationRange(int minMinutes, int maxMinutes) throws SQLException {
        return movieDao.getAllMovies().stream()
                .filter(m -> m.getDurationMinutes() >= minMinutes && m.getDurationMinutes() <= maxMinutes)
                .collect(Collectors.toList());
    }

    // 2.) Maximum rental price
    public List<Movie> findByMaxPrice(double maxPrice) throws SQLException {
        return movieDao.getAllMovies().stream()
                .filter(m -> m.getRentalPricePerDay() <= maxPrice)
                .collect(Collectors.toList());
    }

    // 3.) Specific title and director
    public List<Movie> findByTitleAndDirector(String title, String director) throws SQLException {
        return movieDao.getAllMovies().stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(title)
                        && m.getDirector().equalsIgnoreCase(director))
                .collect(Collectors.toList());
    }
}

