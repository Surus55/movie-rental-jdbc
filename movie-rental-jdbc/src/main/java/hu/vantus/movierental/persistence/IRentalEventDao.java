package hu.vantus.movierental.persistence;

import hu.vantus.movierental.model.RentalEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IRentalEventDao {
    void addRentalEvent(RentalEvent event) throws SQLException;
    List<RentalEvent> getAllRentalEvents() throws SQLException;
    void updateReturnDateAndClose(int rentalId, LocalDate returnDate, double totalCost) throws SQLException;
    void deleteRentalEvent(int id) throws SQLException;
}

