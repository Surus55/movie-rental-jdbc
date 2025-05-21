package hu.vantus.movierental.persistence;

import hu.vantus.movierental.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDao implements IMovieDao {
    private final Connection connection;

    public MovieDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, director, release_year, genre, rental_price_per_day, available, duration_minutes, age_rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getDirector());
            stmt.setInt(3, movie.getReleaseYear());
            stmt.setString(4, movie.getGenre());
            stmt.setDouble(5, movie.getRentalPricePerDay());
            stmt.setBoolean(6, movie.isAvailable());
            stmt.setInt(7, movie.getDurationMinutes());
            stmt.setString(8, movie.getAgeRating());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("release_year"),
                        rs.getString("genre"),
                        rs.getDouble("rental_price_per_day"),
                        rs.getBoolean("available"),
                        rs.getInt("duration_minutes"),
                        rs.getString("age_rating")
                );
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public Movie getMovieById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("director"),
                            rs.getInt("release_year"),
                            rs.getString("genre"),
                            rs.getDouble("rental_price_per_day"),
                            rs.getBoolean("available"),
                            rs.getInt("duration_minutes"),
                            rs.getString("age_rating")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET title = ?, director = ?, release_year = ?, genre = ?, rental_price_per_day = ?, available = ?, duration_minutes = ?, age_rating = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getDirector());
            stmt.setInt(3, movie.getReleaseYear());
            stmt.setString(4, movie.getGenre());
            stmt.setDouble(5, movie.getRentalPricePerDay());
            stmt.setBoolean(6, movie.isAvailable());
            stmt.setInt(7, movie.getDurationMinutes());
            stmt.setString(8, movie.getAgeRating());
            stmt.setInt(9, movie.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteMovie(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

