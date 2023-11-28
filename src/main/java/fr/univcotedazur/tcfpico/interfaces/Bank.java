package fr.univcotedazur.tcfpico.interfaces;

import fr.univcotedazur.tcfpico.entities.Customer;

public interface Bank {

    boolean pay(Customer customer, double value);
}
