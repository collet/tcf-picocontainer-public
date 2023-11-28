package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.entities.Item;
import fr.univcotedazur.tcfpico.entities.Order;
import fr.univcotedazur.tcfpico.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.tcfpico.exceptions.EmptyCartException;
import fr.univcotedazur.tcfpico.exceptions.NegativeQuantityException;
import fr.univcotedazur.tcfpico.exceptions.PaymentException;
import fr.univcotedazur.tcfpico.interfaces.CartModifier;
import fr.univcotedazur.tcfpico.interfaces.CartProcessor;
import fr.univcotedazur.tcfpico.interfaces.CustomerFinder;
import fr.univcotedazur.tcfpico.interfaces.Payment;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CartHandler implements CartModifier, CartProcessor {

    private final CustomerFinder customerFinder;

    private final Payment payment;

    public CartHandler(CustomerFinder customerFinder, Payment payment) {
        this.customerFinder = customerFinder;
        this.payment = payment;
    }

    @Override
    public Item update(UUID customerId, Item item) throws NegativeQuantityException, CustomerIdNotFoundException {
        Customer customer = customerFinder.findById(customerId).orElseThrow(() -> new CustomerIdNotFoundException(customerId));
        int newQuantity = item.getQuantity();
        Set<Item> items = customer.getCart();
        Optional<Item> existing = items.stream().filter(e -> e.getCookie().equals(item.getCookie())).findFirst();
        if (existing.isPresent()) {
            newQuantity += existing.get().getQuantity();
        }
        if (newQuantity < 0) {
            throw new NegativeQuantityException(customer.getName(), item.getCookie(), newQuantity);
        } else {
            existing.ifPresent(items::remove);
            if (newQuantity > 0) {
                items.add(new Item(item.getCookie(), newQuantity));
            }
        }
        return new Item(item.getCookie(), newQuantity);
    }

    @Override
    public Set<Item> getCartContent(UUID customerId) throws CustomerIdNotFoundException {
        return customerFinder.findById(customerId).orElseThrow(() -> new CustomerIdNotFoundException(customerId))
                .getCart();
    }

    @Override
    public double cartPrice(UUID customerId) throws CustomerIdNotFoundException {
        return getCartContent(customerId).stream().mapToDouble(item -> item.getQuantity() * item.getCookie().getPrice())
                .sum();
    }

    @Override
    public Order validate(UUID customerId) throws EmptyCartException, CustomerIdNotFoundException, PaymentException {
        Customer customer = customerFinder.findById(customerId).orElseThrow(() -> new CustomerIdNotFoundException(customerId));
        if (customer.getCart().isEmpty())
            throw new EmptyCartException(customer.getName());
        Order newOrder = payment.payOrderFromCart(customer);
        customer.setCart(new HashSet<>());
        return newOrder;
    }

}
