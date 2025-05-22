package hu.vantus.movierental.repository;

import hu.vantus.movierental.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // akár findByEmail is lehet itt majd
}

