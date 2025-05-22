package hu.vantus.movierental.controller;

import hu.vantus.movierental.model.RentalEvent;
import hu.vantus.movierental.service.RentalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<RentalEvent> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @PostMapping("/rent")
    public RentalEvent rentMovie(@RequestParam Long customerId, @RequestParam Long movieId) {
        return rentalService.rentMovie(customerId, movieId);
    }

    @PostMapping("/return")
    public RentalEvent returnMovie(@RequestParam Long rentalId) {
        return rentalService.returnMovie(rentalId);
    }
}

