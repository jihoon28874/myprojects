package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class GenNode {
        private T item;
        private GenNode previous;
        private GenNode next;

        public GenNode(T item, GenNode p, GenNode n) {
            this.item = item;
            this.next = n;
            this.previous = p;
        }
    }

    private GenNode sentinel;
    private int size;
    private GenNode last;

    //makes empty linkedlist
    public LinkedListDeque() {
        size = 0;
        sentinel = new GenNode((T) (null), null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
    }

    //Adds an item of type T to the front of the deque. You can assume that item is never null.
    @Override
    public void addFirst(T item) {
        sentinel.next = new GenNode(item, sentinel, sentinel.next);
        sentinel.next.next.previous = sentinel.next;
        size = size + 1;
    }

    //Adds an item of type T to the back of the deque. You can assume that item is never null.
    @Override
    public void addLast(T item) {
        sentinel.previous = new GenNode(item, sentinel.previous, sentinel);
        sentinel.previous.previous.next = sentinel.previous;
        size = size + 1;
    }

    @Override
    public int size() {
        return size;
    }

    //Prints the items in the deque from first to last, separated by
    // a space. Once all the items have been printed, print out a new line.
    @Override
    public void printDeque() {
        while (sentinel.next != sentinel) {
            System.out.print(sentinel.next.item);
            System.out.print(" ");
            sentinel.next = sentinel.next.next;
        }
        System.out.println();
    }

    // Removes and returns the item at the front of the deque. If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T temp = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.previous = sentinel;
            size = size - 1;
            return temp;
        }
    }

    @Override

    // Removes and returns the item at the back of the deque. If no such item exists, returns null.
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            T temp = sentinel.previous.item;
            sentinel.previous.previous.next = sentinel;
            sentinel.previous = sentinel.previous.previous;
            size = size - 1;
            return temp;
        }
    }

    @Override

    //Gets the item at the given index, where 0 is the front, 1 is the next item,
    // and so forth. If no such item exists, returns null. Must not alter the deque!
    public T get(int index) {
        GenNode ls = sentinel;
        int k = 0;
        while (ls.next != sentinel) {
            ls = ls.next;
            if (k == index) {
                return ls.item;
            }
            k = k + 1;
        }
        return null;
    }

    //public boolean equals(Object o){}
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        } else {
            return getRecursiveHelper(index, sentinel.next);
        }
    }

    private T getRecursiveHelper(int index, GenNode ls) {
        if (index == 0) {
            return ls.item;
        } else {
            return getRecursiveHelper(index - 1, ls.next);
        }
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
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private int curPos;

        private LinkedListIterator() {
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
