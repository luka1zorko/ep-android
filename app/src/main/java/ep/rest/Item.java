package ep.rest;

import java.io.Serializable;
import java.util.Locale;

public class Item implements Serializable {
    public int Item_Id;
    public String Item_Name, Item_Description, uri;
    public double Item_Price;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,
                "%d: %s (%.2f EUR)", Item_Id, Item_Name, Item_Price);
    }
}
