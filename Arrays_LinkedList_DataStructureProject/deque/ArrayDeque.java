package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int OBJSIZE = 8;

    private static final int RESIZEVAL = 4;

    private static final int MAXSIZE = 16;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[OBJSIZE];
        nextFirst = 0;
        nextLast = 1;
    }

    // its going to make a new array so it is going
    // to be two times the first array and copies everything
    private void resizeUp(int newSize) {
        T[] newArray = (T[]) new Object[newSize];
        int newFirstPos = newArray.length / 2 - items.length / 2;
        int temp = newArray.length / 2 - items.length / 2;
        int newIndex = nextFirst + 1;
        for (int i = 0; i < items.length; i++) {
            newArray[newFirstPos] = items[newIndex % items.length];
            newFirstPos = newFirstPos + 1;
            newIndex = newIndex + 1;
        }
        items = newArray;
        nextFirst = temp - 1;
        nextLast = newFirstPos;
    }

    // its going to make a new array so it is going
    // to be half times the first array and copies everything
    private void resizeDown(int newSize) {
        T[] newArray = (T[]) new Object[newSize];
        int newFirstPos = newArray.length / 2 - size() / 2;
        int temp = newArray.length / 2 - size() / 2;
        int newIndex = nextFirst + 1;
        for (int i = 0; i < size(); i++) {
            newArray[newFirstPos] = items[newIndex % items.length];
            newFirstPos = newFirstPos + 1;
            newIndex = newIndex + 1;
        }
        items = newArray;
        nextFirst = temp - 1;
        nextLast = newFirstPos;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resizeUp(items.length * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size = size + 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resizeUp(items.length * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size = size + 1;
    }

    @Override
    public int size() {
        return this.size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (items.length >= MAXSIZE && size < items.length / RESIZEVAL) {
            resizeDown(items.length / 2);
        }
        T index;
        nextFirst = (nextFirst + 1) % items.length;
        index = items[nextFirst];
        items[nextFirst] = null;
        size = size - 1;
        return index;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (items.length >= MAXSIZE && size < items.length / RESIZEVAL) {
            resizeDown(items.length / 2);
        }
        T index;
        nextLast = (nextLast - 1 + items.length) % items.length;
        index = items[nextLast];
        items[nextLast] = null;
        size = size - 1;
        return index;
    }

    @Override
    public T get(int index) {
        return items[(nextFirst + index + 1) % items.length];
    }

    // logic came from 2019 spring mt1
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque<?>)) {
            return false;
        }
        Deque<T> comO = (Deque<T>) o;
        if (comO.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(this.get(i).equals((comO.get(i))))) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    //Lecture 11 inheritance 4
    private class ArrayDequeIterator implements Iterator<T> {
        private int curPos;

        public ArrayDequeIterator() {
            curPos = 0;
        }

        public boolean hasNext() {
            return curPos < size;
        }

        public T next() {
            T item = get(curPos);
            curPos += 1;
            return item;
        }
    }
}
