package fr.univcotedazur.tcfpico;

import fr.univcotedazur.tcfpico.components.*;
import fr.univcotedazur.tcfpico.connectors.BankProxy;
import fr.univcotedazur.tcfpico.repositories.CustomerRepository;
import fr.univcotedazur.tcfpico.repositories.OrderRepository;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;

public class TestingPicoContainer {

    // One container containing all components for testing purposes
    // (could be refined in several methods with diverse containers)
    public static MutablePicoContainer getFreshCompleteContainer() {
        MutablePicoContainer container = new DefaultPicoContainer(new Caching()); // Caching -> Singleton behavior for components being assembled

        container.addComponent(CustomerRepository.class);
        container.addComponent(OrderRepository.class);

        container.addComponent(CartHandler.class);
        container.addComponent(CustomerRegistry.class);
        container.addComponent(Catalog.class);
        container.addComponent(Cashier.class);
        container.addComponent(Kitchen.class);
        container.addComponent(BankProxy.class);

        return container;
    }

}
