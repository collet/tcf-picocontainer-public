package fr.univcotedazur.tcfpico.controllers;


import fr.univcotedazur.tcfpico.dto.CustomerDTO;
import fr.univcotedazur.tcfpico.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.tcfpico.interfaces.CustomerRegistration;

public class CustomerCareController {

    private final CustomerRegistration registry;

    public CustomerCareController(CustomerRegistration registry) {
        this.registry = registry;
    }

    public CustomerDTO register(String name, String creditCard) { // should check input
        if (name == null || creditCard == null) {
            throw new IllegalArgumentException("Name and credit card must be provided");
        }
        try {
            return registry.register(name, creditCard);
        } catch (AlreadyExistingCustomerException e) {
            throw new IllegalArgumentException("Customer already exists");
        }
    }


}

