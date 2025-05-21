package hu.vantus.movierental.persistence;

import hu.vantus.movierental.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerDao {
    void addCustomer(Customer customer) throws SQLException;
    List<Customer> getAllCustomers() throws SQLException;
    Customer getCustomerById(int id) throws SQLException;
    void updateCustomer(Customer customer) throws SQLException;
    void deleteCustomer(int id) throws SQLException;
}

