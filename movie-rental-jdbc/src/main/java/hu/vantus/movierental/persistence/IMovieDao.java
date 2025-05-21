package hu.vantus.movierental.persistence;

import hu.vantus.movierental.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDao {
    void addMovie(Movie movie) throws SQLException;
    List<Movie> getAllMovies() throws SQLException;
    Movie getMovieById(int id) throws SQLException;
    void updateMovie(Movie movie) throws SQLException;
    void deleteMovie(int id) throws SQLException;
}

