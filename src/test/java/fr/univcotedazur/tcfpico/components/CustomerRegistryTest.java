package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.TestingPicoContainer;
import fr.univcotedazur.tcfpico.dto.CustomerDTO;
import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.tcfpico.interfaces.CustomerFinder;
import fr.univcotedazur.tcfpico.interfaces.CustomerRegistration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.picocontainer.MutablePicoContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegistryTest {

    private CustomerRegistration customerRegistration;

    private CustomerFinder customerFinder;

    private String name = "John";
    private String creditCard = "credit card number";

    @BeforeEach
    void setUp() {
        MutablePicoContainer container = TestingPicoContainer.getFreshCompleteContainer();
        customerRegistration = container.getComponent(CustomerRegistration.class);
        customerFinder = container.getComponent(CustomerFinder.class);
        // No need to clear the repository, as it is a fresh one each time
        // customerRepository.deleteAll();
    }

    @Test
    void unknownCustomer() {
        assertFalse(customerFinder.findByName(name).isPresent());
    }

    @Test
    void registerCustomer() throws Exception {
        CustomerDTO returnedDTO = customerRegistration.register(name, creditCard);
        Optional<Customer> customer = customerFinder.findByName(name);
        assertTrue(customer.isPresent());
        Customer john = customer.get();
        assertEquals(john, customerFinder.findById(returnedDTO.getId()).get());
        assertEquals(name, john.getName());
        assertEquals(name, returnedDTO.getName());
        assertEquals(creditCard, john.getCreditCard());
        assertEquals(creditCard, returnedDTO.getCreditCard());
    }

    @Test
    void cannotRegisterTwice() throws Exception {
        customerRegistration.register(name, creditCard);
        Assertions.assertThrows(AlreadyExistingCustomerException.class, () -> {
            customerRegistration.register(name, creditCard);
        });
    }

}