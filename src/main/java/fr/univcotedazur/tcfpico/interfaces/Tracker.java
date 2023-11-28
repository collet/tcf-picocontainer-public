package fr.univcotedazur.tcfpico.interfaces;

import fr.univcotedazur.tcfpico.entities.OrderStatus;
import fr.univcotedazur.tcfpico.exceptions.UnknownOrderId;

import java.util.UUID;

public interface Tracker {

    OrderStatus retrieveStatus(UUID orderId) throws UnknownOrderId;

}