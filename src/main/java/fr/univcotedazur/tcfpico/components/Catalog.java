package fr.univcotedazur.tcfpico.components;


import fr.univcotedazur.tcfpico.entities.Cookies;
import fr.univcotedazur.tcfpico.interfaces.CatalogExplorator;

import java.util.EnumSet;
import java.util.Set;

public class Catalog implements CatalogExplorator {

    @Override
    public Set<Cookies> listPreMadeRecipes() {
        return EnumSet.allOf(Cookies.class);
    }


}
