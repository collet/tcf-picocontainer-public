package fr.univcotedazur.tcfpico.interfaces;

import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.entities.Item;
import fr.univcotedazur.tcfpico.entities.Order;
import fr.univcotedazur.tcfpico.exceptions.PaymentException;

import java.util.Set;

public interface Payment {

    Order payOrderFromCart(Customer customer) throws PaymentException;

}
