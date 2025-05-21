package hu.vantus.movierental.service;

import hu.vantus.movierental.model.Movie;
import hu.vantus.movierental.model.RentalEvent;
import hu.vantus.movierental.persistence.MovieDao;
import hu.vantus.movierental.persistence.RentalEventDao;

import java.sql.SQLException;
import java.time.LocalDate;

public class RentalEventService {

    private final RentalEventDao eventDao;
    private final MovieDao movieDao;

    public RentalEventService(RentalEventDao eventDao, MovieDao movieDao) {
        this.eventDao = eventDao;
        this.movieDao = movieDao;
    }

    /** 1. Availability Check + Rental **/
    public void rentMovie(int movieId, int customerId, LocalDate rentalDate) throws SQLException {
        Movie movie = movieDao.getMovieById(movieId);
        if (movie == null) throw new IllegalArgumentException("Film nem található: " + movieId);
        if (!movie.isAvailable())    throw new IllegalStateException("Film nem elérhető");
        // létrehozunk egy RentalEvent-et
        RentalEvent ev = new RentalEvent(0, movieId, customerId, rentalDate, null, 0.0, false);
        eventDao.addRentalEvent(ev);
        // és beállítjuk, hogy ne legyen elérhető
        movie.setAvailable(false);
        movieDao.updateMovie(movie);
    }

    /** 2. Return Process **/
    public void returnMovie(int rentalId, LocalDate returnDate, double totalCost) throws SQLException {
        // frissítjük a rental event-et
        eventDao.updateReturnDateAndClose(rentalId, returnDate, totalCost);
        // visszakapjuk az eseményt, hogy tudjuk a movieId-t
        RentalEvent ev = eventDao.getAllRentalEvents().stream()
                .filter(e -> e.getId() == rentalId)
                .findFirst()
                .orElseThrow();
        // és visszaállítjuk a film elérhetőségét
        Movie movie = movieDao.getMovieById(ev.getMovieId());
        movie.setAvailable(true);
        movieDao.updateMovie(movie);
    }
}

