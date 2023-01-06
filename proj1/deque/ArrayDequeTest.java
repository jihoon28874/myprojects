package deque;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addLastTest() {
        ArrayDeque b = new ArrayDeque<Integer>();
        b.addLast(2);
        b.addLast(2);
        b.addLast(2);
        b.addLast(2);
        b.addLast(2);
        b.addLast(2);
    }

    @Test
    public void addFirstTest() {
        ArrayDeque c = new ArrayDeque<Integer>();
        c.addFirst(2);
        c.addFirst(2);
        c.addFirst(2);
        c.addFirst(2);
        c.addFirst(2);
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque d = new ArrayDeque<Integer>();
        d.addLast(3);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        d.removeFirst();
        assertEquals(d.get(0), 2);
    }

    @Test
    public void removeLastTest() {
        ArrayDeque a = new ArrayDeque<Integer>();
        a.addFirst(5);
        a.addFirst(3);
        a.addFirst(3);
        a.addFirst(2);
        a.addFirst(3);
        a.addFirst(7);
        a.addLast(8);
        a.removeLast();
        assertEquals(a.get(5), 5);
    }

    @Test
    public void printDequeTest() {
        ArrayDeque a = new ArrayDeque<Integer>();
        a.addLast(5);
        a.addLast(4);
        a.addLast(3);
        a.addLast(2);
        a.addLast(1);
        a.addLast(5);
        a.addLast(2);
        a.addLast(3);
        a.printDeque();
    }

    @Test
    public void resizeTest() {
        ArrayDeque a = new ArrayDeque<Integer>();
        int k = 64;
        for(int i = 0; i < k; i++){
            a.addFirst(i);
            a.addLast(i);
        }
        for(int i = 0; i < k; i++){
            a.removeLast();
        }
    }

    @Test
    public void resizeTestDecrease() {
        ArrayDeque a = new ArrayDeque<Integer>();
        a.addLast(5);
        a.addLast(4);
        a.addLast(3);
        a.addLast(2);
        a.addLast(1);
        a.addLast(5);
        a.addLast(2);
        a.addLast(3);
        a.addLast(5);
        a.addLast(4);
        a.addLast(3);
        a.addLast(2);
        a.addLast(1);
        a.addLast(5);
        a.addLast(2);
        a.addLast(2);
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.addLast(5);
        a.addLast(5);
        a.addLast(5);
        a.addFirst(4);
        a.printDeque();
    }

    @Test
    public void equalsTest() {
        int k = 500;
        Deque<Integer> a = new ArrayDeque<Integer>();
        Deque<Integer> b = new ArrayDeque<Integer>();
        Deque<Integer> c = new LinkedListDeque<Integer>();
        for (int i = 0; i < k; i++) {
            a.addLast(2);
        }
        for (int i = 0; i < k; i++) {
            b.addLast(2);
        }
        for (int i = 0; i <= k; i++) {
            c.addFirst(2);
        }
        System.out.println(a.equals(b));
        System.out.println(b.equals(c));
    }

    @Test
    public void IteratorTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        LinkedListDeque<Integer> b = new LinkedListDeque<>();
        int k = 100;
        for(int i = 0; i < 100; i++){
            a.addLast(i);
        }
        a.iterator();
    }

    @Test
    public void equalTester() {
        ArrayDeque b = new ArrayDeque<Integer>();
        b.addFirst(5);
        b.addLast(3);
        b.addFirst(2);
        LinkedListDeque c = new LinkedListDeque<Integer>();
        c.addFirst(5);
        c.addLast(3);
        c.addFirst(2);
        assertTrue(b.equals(c));
    }

}

