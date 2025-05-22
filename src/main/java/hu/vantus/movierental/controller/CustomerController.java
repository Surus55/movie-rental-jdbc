package hu.vantus.movierental.controller;

import hu.vantus.movierental.model.Customer;
import hu.vantus.movierental.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.findAll(); //
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        Customer customer = customerService.findById(id); //
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }
        return customer;
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.save(customer); //
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id); //
    }
}


