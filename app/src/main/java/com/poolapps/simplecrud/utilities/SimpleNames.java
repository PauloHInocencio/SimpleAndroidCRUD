package com.poolapps.simplecrud.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulo on 1/29/2017.
 */

public class SimpleNames {

   public static final class Name {
       String firstName;
       String listName;

       public Name(String firstName, String listName) {
           this.firstName = firstName;
           this.listName = listName;
       }

       public String getFirstName() {
           return firstName;
       }

       public void setFirstName(String firstName) {
           this.firstName = firstName;
       }

       public String getListName() {
           return listName;
       }

       public void setListName(String listName) {
           this.listName = listName;
       }
   }

    public static List<Name> names;

    static {
        names = new ArrayList<>();
        names.add(new Name("Paulo", "Inocencio"));
        names.add(new Name("Renata", "Mola"));
        names.add(new Name("Carlos", "Mola"));
        names.add(new Name("Roberto", "Melo"));
        names.add(new Name("Wagner", "Inocencio"));
        names.add(new Name("Debora", "Inocencio"));
        names.add(new Name("Rodrigo", "Edamatsu"));
        names.add(new Name("Lucas", "Flavio"));
        names.add(new Name("Willian", "Oliveira"));
        names.add(new Name("Gabriel", "Chuck"));
        names.add(new Name("Rodnald", "Robertsons"));
        names.add(new Name("Rangel", "Pedon"));
        names.add(new Name("Marcelo", "Souza"));
        names.add(new Name("Lucas", "Pereira"));
        names.add(new Name("Lucas", "Rosa"));
        names.add(new Name("Rafael", "Odassi"));
        names.add(new Name("Rafael", "Oliveira"));
        names.add(new Name("Sônia", "Inocêncio"));
        names.add(new Name("João", "Rosa"));
    }
}
