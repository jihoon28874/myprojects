package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;
    }

    //return max element in the deque
    //if empty returns null
    //logic comes from comparator lecture
    public T max() {
        if (isEmpty()) {
            return null;
        }
        T compItem = this.get(0);
        for (T i : this) {
            if (comp.compare(compItem, i) < 0) {
                compItem = i;
            }
        }
        return compItem;
    }

    // returns max element in deque
    // by parameter comparator c
    // if empty returns null
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T compItem = this.get(0);
        for (T i : this) {
            if (c.compare(compItem, i) < 0) {
                compItem = i;
            }
        }
        return compItem;
    }
}
