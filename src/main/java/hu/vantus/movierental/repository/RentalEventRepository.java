package hu.vantus.movierental.repository;

import hu.vantus.movierental.model.RentalEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalEventRepository extends JpaRepository<RentalEvent, Long> {
    // később pl. findByCustomerId vagy findByMovieId
}
