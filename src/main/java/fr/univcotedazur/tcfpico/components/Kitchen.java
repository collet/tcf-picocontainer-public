package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.entities.Order;
import fr.univcotedazur.tcfpico.entities.OrderStatus;
import fr.univcotedazur.tcfpico.exceptions.UnknownOrderId;
import fr.univcotedazur.tcfpico.interfaces.OrderProcessing;
import fr.univcotedazur.tcfpico.interfaces.Tracker;
import fr.univcotedazur.tcfpico.repositories.OrderRepository;

import java.util.Optional;
import java.util.UUID;

public class Kitchen implements OrderProcessing, Tracker {

    OrderRepository orderRepository;

    public Kitchen(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void process(Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
    }

    @Override
    public OrderStatus retrieveStatus(UUID orderId) throws UnknownOrderId {
        return orderRepository.findById(orderId).orElseThrow(() -> new UnknownOrderId(orderId)).getStatus();
    }

}
