package fr.univcotedazur.tcfpico.interfaces;

import fr.univcotedazur.tcfpico.entities.Item;
import fr.univcotedazur.tcfpico.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.tcfpico.exceptions.NegativeQuantityException;

import java.util.Set;
import java.util.UUID;

public interface CartModifier {

    Item update(UUID customerId, Item it) throws NegativeQuantityException, CustomerIdNotFoundException;

    Set<Item> getCartContent(UUID customerId) throws CustomerIdNotFoundException;

}
