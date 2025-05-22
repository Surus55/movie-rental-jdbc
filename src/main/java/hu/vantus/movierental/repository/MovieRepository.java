package hu.vantus.movierental.repository;

import hu.vantus.movierental.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // plusz lekérdezések is jöhetnek majd, pl. findByTitle stb.
}

