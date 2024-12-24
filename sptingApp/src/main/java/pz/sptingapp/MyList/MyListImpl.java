package pz.sptingapp.MyList;
import java.util.Iterator;

public class MyListImpl<T> implements IMyList<T> {
    private class Node {
        T data;
        Node next;
        Node prev;

        Node(T data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    @Override
    public void add(T e) {
        Node newNode = new Node(e);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean remove(T o) {
        Node current = head;
        while (current != null) {
            if (current.data.equals(o)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T o) {
        Node current = head;
        while (current != null) {
            if (current.data.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Node current = head;
        while (current != null) {
            sb.append("[").append(current.data.toString()).append("]");
            current = current.next;
            if (current != null) sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    private class IteratorImpl implements Iterator<T> {
        private Node current = head;
        private Node lastReturned = null;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            lastReturned = current;
            current = current.next;
            return lastReturned.data;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            if (lastReturned.prev != null) {
                lastReturned.prev.next = lastReturned.next;
            } else {
                head = lastReturned.next;
            }
            if (lastReturned.next != null) {
                lastReturned.next.prev = lastReturned.prev;
            } else {
                tail = lastReturned.prev;
            }
            size--;
            lastReturned = null;
        }
    }
}
