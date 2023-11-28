package fr.univcotedazur.tcfpico.connectors;

import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.interfaces.Bank;

public class BankProxy implements Bank {

    @Override
    public boolean pay(Customer customer, double value) {
        // should be an external connection to a bank service (with kind of PaymentDTO)
        return (value > 0);
    }
}