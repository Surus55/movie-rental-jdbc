package hu.vantus.movierental.model;

import java.time.LocalDate;

public class RentalEvent {
    private int id;
    private int movieId;
    private int customerId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double totalCost;
    private boolean isClosed;

    public RentalEvent() {}

    public RentalEvent(int id, int movieId, int customerId, LocalDate rentalDate,
                       LocalDate returnDate, double totalCost, boolean isClosed) {
        this.id = id;
        this.movieId = movieId;
        this.customerId = customerId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.totalCost = totalCost;
        this.isClosed = isClosed;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Override
    public String toString() {
        return "RentalEvent{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", customerId=" + customerId +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", totalCost=" + totalCost +
                ", isClosed=" + isClosed +
                '}';
    }
}
