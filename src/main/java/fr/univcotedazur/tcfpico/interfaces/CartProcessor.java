package fr.univcotedazur.tcfpico.interfaces;

import fr.univcotedazur.tcfpico.entities.Order;
import fr.univcotedazur.tcfpico.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.tcfpico.exceptions.EmptyCartException;
import fr.univcotedazur.tcfpico.exceptions.PaymentException;

import java.util.UUID;

public interface CartProcessor {

    double cartPrice(UUID customerId) throws CustomerIdNotFoundException;

    Order validate(UUID customerId) throws EmptyCartException, CustomerIdNotFoundException, PaymentException;

}
