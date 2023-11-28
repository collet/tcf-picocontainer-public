package fr.univcotedazur.tcfpico.interfaces;

import fr.univcotedazur.tcfpico.entities.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerFinder {

    Optional<Customer> findByName(String name);

    Optional<Customer> findById(UUID id);

}
