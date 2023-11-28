package fr.univcotedazur.tcfpico.entities;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Order {

    private UUID id;

    private Customer customer;

    private Set<Item> items;

    private OrderStatus status;

    public Order(Customer customer, Set<Item> items) {
        this.customer = customer;
        this.items = items;
        this.status = OrderStatus.VALIDATED;
        this.id = UUID.randomUUID();
    }

    public Order() {
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Set<Item> getItems() {
        return items;
    }

    public double getPrice() {
        return items.stream().mapToDouble(item -> item.getQuantity() * item.getCookie().getPrice()).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(items, order.items) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, items, status);
    }
}
