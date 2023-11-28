package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.dto.CustomerDTO;
import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.tcfpico.interfaces.CustomerFinder;
import fr.univcotedazur.tcfpico.interfaces.CustomerRegistration;
import fr.univcotedazur.tcfpico.repositories.CustomerRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class CustomerRegistry implements CustomerRegistration, CustomerFinder {

    private final CustomerRepository customerRepository;

    public CustomerRegistry(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO register(String name, String creditCard)
            throws AlreadyExistingCustomerException {
        if (findByName(name).isPresent())
            throw new AlreadyExistingCustomerException(name);
        Customer newcustomer = new Customer(name, creditCard);
        return convertCustomerToDto(customerRepository.save(newcustomer, newcustomer.getId()));
    }

    @Override
    public Optional<Customer> findByName(String name) {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .filter(cust -> name.equals(cust.getName())).findAny();
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }

    private CustomerDTO convertCustomerToDto(Customer customer) { // In more complex cases, we could use ModelMapper
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard());
    }

}
