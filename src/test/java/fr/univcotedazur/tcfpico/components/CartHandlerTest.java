package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.TestingPicoContainer;
import fr.univcotedazur.tcfpico.entities.Cookies;
import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.entities.Item;
import fr.univcotedazur.tcfpico.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.tcfpico.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.tcfpico.exceptions.EmptyCartException;
import fr.univcotedazur.tcfpico.exceptions.NegativeQuantityException;
import fr.univcotedazur.tcfpico.interfaces.CartModifier;
import fr.univcotedazur.tcfpico.interfaces.CartProcessor;
import fr.univcotedazur.tcfpico.interfaces.CustomerFinder;
import fr.univcotedazur.tcfpico.interfaces.CustomerRegistration;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.util.LRUMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.picocontainer.MutablePicoContainer;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartHandlerTest {

    private CustomerRegistration customerRegistration;
    private CustomerFinder customerFinder;
    private CartModifier cartModifier;
    private CartProcessor cartProcessor;

    private UUID johnId;

    @BeforeEach
    void setUp() throws AlreadyExistingCustomerException {
        MutablePicoContainer container = TestingPicoContainer.getFreshCompleteContainer();
        customerRegistration = container.getComponent(CustomerRegistration.class);
        customerFinder = container.getComponent(CustomerFinder.class);
        cartModifier = container.getComponent(CartModifier.class);
        cartProcessor = container.getComponent(CartProcessor.class);

        johnId = customerRegistration.register("John", "1234567890").getId();
    }

    @Test
    void emptyCartByDefault() throws CustomerIdNotFoundException {
        assertEquals(0, cartModifier.getCartContent(johnId).size());
    }

    @Test
    void addItems() throws NegativeQuantityException, CustomerIdNotFoundException {
        Item itemResult = cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 2));
        assertEquals(new Item(Cookies.CHOCOLALALA, 2), itemResult);
        cartModifier.update(johnId, new Item(Cookies.DARK_TEMPTATION, 3));
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3));
        assertEquals(oracle, cartModifier.getCartContent(johnId));
    }

    @Test
    void removeItems() throws NegativeQuantityException, CustomerIdNotFoundException {
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 2));
        Item itemResult = cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, -2));
        assertEquals(new Item(Cookies.CHOCOLALALA, 0), itemResult);
        assertEquals(0, cartModifier.getCartContent(johnId).size());
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 6));
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, -5));
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 1));
        assertEquals(oracle, cartModifier.getCartContent(johnId));
    }

    @Test
    void removeTooMuchItems() throws NegativeQuantityException, CustomerIdNotFoundException {
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(johnId, new Item(Cookies.DARK_TEMPTATION, 3));
        Assertions.assertThrows(NegativeQuantityException.class, () -> {
            cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, -3));
        });
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3));
        assertEquals(oracle, cartModifier.getCartContent(johnId));
    }

    @Test
    void modifyQuantities() throws NegativeQuantityException, CustomerIdNotFoundException {
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(johnId, new Item(Cookies.DARK_TEMPTATION, 3));
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 3));
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3));
        assertEquals(oracle, cartModifier.getCartContent(johnId));
    }

    @Test
    void getTheRightPrice() throws NegativeQuantityException, CustomerIdNotFoundException {
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(johnId, new Item(Cookies.DARK_TEMPTATION, 3));
        cartModifier.update(johnId, new Item(Cookies.CHOCOLALALA, 3));
        assertEquals(12.20, cartProcessor.cartPrice(johnId), 0.01);
    }

    @Test
    void cannotProcessEmptyCart() throws Exception {
        assertEquals(0, cartModifier.getCartContent(johnId).size());
        Assertions.assertThrows(EmptyCartException.class, () -> {
            cartProcessor.validate(johnId);
        });
    }

}