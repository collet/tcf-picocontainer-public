package fr.univcotedazur.tcfpico.components;

import fr.univcotedazur.tcfpico.TestingPicoContainer;
import fr.univcotedazur.tcfpico.entities.Cookies;
import fr.univcotedazur.tcfpico.interfaces.CatalogExplorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.picocontainer.MutablePicoContainer;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogTest {

    CatalogExplorator catalog;

    @BeforeEach
    void setUp() {
        MutablePicoContainer container = TestingPicoContainer.getFreshCompleteContainer();
        catalog = container.getComponent(CatalogExplorator.class);
    }

    @Test
    void listPreMadeRecipesTest() {
        Set<Cookies> premade = catalog.listPreMadeRecipes();
        assertEquals(3, premade.size());
    }


}