package fr.univcotedazur.tcfpico.interfaces;


import fr.univcotedazur.tcfpico.dto.CustomerDTO;
import fr.univcotedazur.tcfpico.exceptions.AlreadyExistingCustomerException;

public interface CustomerRegistration {

    CustomerDTO register(String name, String creditCard)
            throws AlreadyExistingCustomerException;
}
