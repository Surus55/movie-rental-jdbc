package hu.vantus.movierental.model;

public class Movie {
    private int id;
    private String title;
    private String director;
    private int releaseYear;
    private String genre;
    private double rentalPricePerDay;
    private boolean available;
    private int durationMinutes;
    private String ageRating;

    public Movie() {}

    public Movie(int id, String title, String director, int releaseYear, String genre,
                 double rentalPricePerDay, boolean available, int durationMinutes, String ageRating) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rentalPricePerDay = rentalPricePerDay;
        this.available = available;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(double rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", releaseYear=" + releaseYear +
                ", genre='" + genre + '\'' +
                ", rentalPricePerDay=" + rentalPricePerDay +
                ", available=" + available +
                ", durationMinutes=" + durationMinutes +
                ", ageRating='" + ageRating + '\'' +
                '}';
    }
}
