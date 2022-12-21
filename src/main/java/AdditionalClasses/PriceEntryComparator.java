package AdditionalClasses;

import Interfaces.Valuable;
import java.util.Comparator;
import java.util.Map;

public class PriceEntryComparator<K, V extends Valuable> implements Comparator<Map.Entry<K, V>> {

    @Override
    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        double price1 = o1.getValue().getPrice();
        double price2 = o2.getValue().getPrice();
        if (price1 > price2) {
            return 1;
        } else if (price1 == price2) {
            return 0;
        }
        return -1;
    }
}
