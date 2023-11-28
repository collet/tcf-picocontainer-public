package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.entities.Customer;
import fr.univcotedazur.tcfpico.entities.Order;
import fr.univcotedazur.tcfpico.exceptions.PaymentException;
import fr.univcotedazur.tcfpico.interfaces.Bank;
import fr.univcotedazur.tcfpico.interfaces.OrderProcessing;
import fr.univcotedazur.tcfpico.interfaces.Payment;
import fr.univcotedazur.tcfpico.repositories.OrderRepository;

import java.util.HashSet;

public class Cashier implements Payment {

    private Bank bank;

    private OrderProcessing kitchen;

    private OrderRepository orderRepository;

    public Cashier(Bank bank, OrderProcessing kitchen, OrderRepository orderRepository) {
        this.bank = bank;
        this.kitchen = kitchen;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order payOrderFromCart(Customer customer) throws PaymentException {
        Order order = new Order(customer, new HashSet<>(customer.getCart()));
        double price = order.getPrice();
        boolean status = false;
        status = bank.pay(customer, price);
        if (!status) {
            throw new PaymentException(customer.getName(), price);
        }
        orderRepository.save(order,order.getId());
        kitchen.process(order);
        return order;
    }

}
