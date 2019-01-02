package ep.rest;

import java.io.Serializable;
import java.util.Locale;

public class Item implements Serializable {
    public int id;
    public String name;
    public String description;
    public double price;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,
                "%s: (%.2f EUR)", name, price);
    }
}
