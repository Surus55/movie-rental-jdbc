package hu.vantus.movierental.persistence;

import hu.vantus.movierental.model.RentalEvent;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalEventDao implements IRentalEventDao {
    private final Connection connection;

    public RentalEventDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRentalEvent(RentalEvent event) throws SQLException {
        String sql = "INSERT INTO rental_events (movie_id, customer_id, rental_date, return_date, total_cost, is_closed) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, event.getMovieId());
            stmt.setInt(2, event.getCustomerId());
            stmt.setDate(3, Date.valueOf(event.getRentalDate()));
            if (event.getReturnDate() != null) {
                stmt.setDate(4, Date.valueOf(event.getReturnDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setDouble(5, event.getTotalCost());
            stmt.setBoolean(6, event.isClosed());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<RentalEvent> getAllRentalEvents() throws SQLException {
        List<RentalEvent> events = new ArrayList<>();
        String sql = "SELECT * FROM rental_events";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                RentalEvent event = new RentalEvent(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getInt("customer_id"),
                        rs.getDate("rental_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        rs.getDouble("total_cost"),
                        rs.getBoolean("is_closed")
                );
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void updateReturnDateAndClose(int rentalId, LocalDate returnDate, double totalCost) throws SQLException {
        String sql = "UPDATE rental_events SET return_date = ?, total_cost = ?, is_closed = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setDouble(2, totalCost);
            stmt.setInt(3, rentalId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteRentalEvent(int id) throws SQLException {
        String sql = "DELETE FROM rental_events WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
