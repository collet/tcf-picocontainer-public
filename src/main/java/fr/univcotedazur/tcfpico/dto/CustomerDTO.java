package fr.univcotedazur.tcfpico.dto;

import java.util.UUID;

public class CustomerDTO {

    private UUID id; // expected to be empty when creating   Customer, and containing the Id when returned

    private String name;

    private String creditCard;

    public CustomerDTO(String name, String creditCard) {
        this.name = name;
        this.creditCard = creditCard;
    }

    public CustomerDTO(UUID id, String name, String creditCard) {
        this(name, creditCard);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

}
