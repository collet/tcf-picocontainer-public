package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.connectors.BankProxy;
import fr.univcotedazur.tcfpico.entities.Cookies;
import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.entities.Item;
import fr.univcotedazur.tcfpico.entities.Order;
import fr.univcotedazur.tcfpico.exceptions.PaymentException;
import fr.univcotedazur.tcfpico.interfaces.Bank;
import fr.univcotedazur.tcfpico.interfaces.Payment;
import fr.univcotedazur.tcfpico.repositories.CustomerRepository;
import fr.univcotedazur.tcfpico.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;

import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CashierTest {

    CustomerRepository customerRepository;

    private Payment cashier;
    private Bank bankMock;

    // Test context
    private Set<Item> items;
    Customer john;
    Customer pat;

    @BeforeEach
    public void setUp() throws Exception {
        // specific pico setup with a Bank mock
        MutablePicoContainer container = new DefaultPicoContainer(new Caching());
        container.addComponent(CustomerRepository.class);
        container.addComponent(OrderRepository.class);
        container.addComponent(Cashier.class);
        container.addComponent(Kitchen.class);
        container.addComponent(Bank.class, mock(Bank.class));
        customerRepository = container.getComponent(CustomerRepository.class);
        cashier = container.getComponent(Payment.class);
        bankMock = container.getComponent(Bank.class);

        items = new HashSet<>();
        items.add(new Item(Cookies.CHOCOLALALA, 3));
        items.add(new Item(Cookies.DARK_TEMPTATION, 2));
        // Customers
        john = new Customer("john", "1234-896983");
        john.setCart(items);
        customerRepository.save(john, john.getId());
        pat  = new Customer("pat", "1234-567890");
        pat.setCart(items);
        customerRepository.save(pat, pat.getId());
        // Mocking the bank proxy
        when(bankMock.pay(eq(john), anyDouble())).thenReturn(true);
        when(bankMock.pay(eq(pat),  anyDouble())).thenReturn(false);
    }

    @Test
    void processToPayment() throws Exception {
        // paying order
        Order order = cashier.payOrderFromCart(john);
        assertNotNull(order);
        assertEquals(john, order.getCustomer());
        assertEquals(items, order.getItems());
        double price = (3 * Cookies.CHOCOLALALA.getPrice()) + (2 * Cookies.DARK_TEMPTATION.getPrice());
        assertEquals(price, order.getPrice(), 0.0);
        assertEquals(2,order.getItems().size());
    }

    @Test
    void identifyPaymentError() {
        Assertions.assertThrows( PaymentException.class, () -> {
            cashier.payOrderFromCart(pat);
        });
    }

}