package org.example;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class TripletDeque<E> implements Deque<E>, Containerable {
    public static final int CONTAINER_SIZE = 5;


    private class Container<E> {
        private Object[] container;
        private Container<E> next;
        private Container<E> prev;
        private int size;
        private int index;

        public Container() {
            this.container = new Object[CONTAINER_SIZE];
        }
    }

    private Container<E> firstC;
    private Container<E> lastC;
    private static final int DEFAULT_CAPACITY_OF_TRIPLETDEQUE = 1000;

    private int capacityOfTripletDeque;

    public TripletDeque() {
        this.capacityOfTripletDeque = DEFAULT_CAPACITY_OF_TRIPLETDEQUE;
    }
    public TripletDeque(int capacityOfTripletDeque) {
        this.capacityOfTripletDeque = capacityOfTripletDeque;
    }
    @Override
    public Object[] getContainerByIndex(int cIndex) {
        if (firstC.index == cIndex) {
            return firstC.container;
        }
        if (lastC. index == cIndex) {
            return lastC.container;
        }
        Container<E>current = firstC.next;
        while (current.index != cIndex) {
            if(current.index == cIndex) {
                return current.container;
            }
            current = current.next;
        }
        return current.container;
    }

    private int getFirstNonNullInLastContainerForRemoveLast(E[] container) {
        for (int i = CONTAINER_SIZE-1;i >=0  ; i--) {
            if (container[i] != null) {
                return i;
            }
        }
        return -1;
    }

    private int getFirstNonNullInFirstContainerForGetFirst(E[] container) {
        for (int i = 0;i <= CONTAINER_SIZE-1 ; i++) {
            if (i ==0 && container[i] != null ) {
                return i;
            }
            if (container[i] == null && container[i+1] != null ) {
                return i+1;
            }
        }
        return -1;
    }


    @Override
    public void addFirst(E e) {
        if(e == null) {
            throw new NullPointerException();
        }
        if (size() >= capacityOfTripletDeque) {
            throw new IllegalStateException("Заданный размер очереди превышен");
        }
        if(isEmpty()) {
            Container<E> newContainer = new Container<>();
            newContainer.size=0;
            firstC = newContainer;
            firstC.index = 0;
            lastC = firstC;
        }
        if(firstC.size == CONTAINER_SIZE) {
            if(firstC.index == 0 && firstC.next == null) {
                Container<E> newContainer = new Container<>();
                newContainer.size = 0;
                newContainer.index = 0;
                firstC.next = newContainer;
                firstC.next.index++;
                firstC.next.container = firstC.container;
                firstC.next.size = CONTAINER_SIZE;
                firstC.next.prev = firstC;
                Container<E> newContainer1 = new Container<>();
                firstC.container = newContainer1.container;
                firstC.index =0;
                firstC.size = 0;
                lastC = firstC.next;
            } else {
                Container<E> current = lastC;
                while(current.prev != null) {
                    if(current.next == null) {
                        current.next = new Container<>();
                        current.next.container = current.container;
                        current.next.index = current.index+1;
                        current.next.size = CONTAINER_SIZE;
                        current.next.prev = current;
                        lastC = current.next;
                    }
                    if(current.prev != null){
                        current.container = current.prev.container;
                    }
                    if(current.prev.index == 0) {
                        Container <E> cont = new Container<>();
                        current.prev.container = cont.container;
                        current.prev.size = 0;
                    }
                    current = current.prev;
                }
            }
        }
        firstC.container[CONTAINER_SIZE-1 - firstC.size] = e;
        firstC.size++;
    }


    @Override
    public void addLast(E e) {
        if(e == null) {
            throw new NullPointerException();
        }
        if (size() >= capacityOfTripletDeque) {
            throw new IllegalStateException("Заданный размер очереди превышен");
        }
        if(isEmpty()) {
            Container<E> newContainer = new Container<>();
            newContainer.size=0;
            firstC = newContainer;
            firstC.index = 0;
            lastC = firstC;
        }
        if(lastC.size == CONTAINER_SIZE) {
            if(lastC == firstC)  {
                firstC.next = new Container<>();
                lastC = firstC.next;
                lastC.index++;
                lastC.prev = firstC;
            } else {
                lastC.next = new Container<>();
                lastC.next.index = lastC.index + 1;
                lastC.next.prev = lastC;
                lastC = lastC.next;
            }
        }
        lastC.container[lastC.size] = e;
        lastC.size++;
    }

    @Override
    public boolean offerFirst(E e) {
        if (isEmpty()) {
            return false;
        }
         addFirst(e);
         return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (isEmpty()) {
            return false;
        }
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        E removing = getFirst() ;
        firstC.container[getFirstNonNullInFirstContainerForGetFirst((E[]) firstC.container)] = null;
        firstC.size--;

        if (firstC.size == 0) {
            if (firstC == lastC) {
                firstC = null;
                lastC = null;
            } else {
                firstC = firstC.next;
            }
        }

        return removing;
    }


    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E removing = getLast() ;
        lastC.container[getFirstNonNullInLastContainerForRemoveLast((E[]) lastC.container)] = null;
        lastC.size--;

        if(lastC.size == 0) {
            if(firstC == lastC) {
                lastC = null;
                firstC = null;
            } else {
                lastC = lastC.prev;
                lastC.next = null;
            }
        }
        return removing;
    }

    @Override
    public E pollFirst() {
        if(isEmpty()) return null;
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if(isEmpty()) return null;
        return removeLast();
    }

    @Override
    public E getFirst() {
        return (E) firstC.container[getFirstNonNullInFirstContainerForGetFirst((E[]) firstC.container)];
    }

    @Override
    public E getLast() {
            return (E) lastC.container[getFirstNonNullInLastContainerForRemoveLast((E[]) lastC.container)];
    }

    @Override
    public E peekFirst() {
        if (isEmpty() ) return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (isEmpty() ) return null;
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        Container<E> currentContainer = firstC;

        while (currentContainer != null) {
            for (int i = 0; i < currentContainer.size; i++) {
                if (Objects.equals(currentContainer.container[i], o)) {
                    System.arraycopy(currentContainer.container, i + 1, currentContainer.container, i, currentContainer.size - i - 1);
                    currentContainer.container[currentContainer.size - 1] = null;
                    currentContainer.size--;

                    if (currentContainer.size == 0) {
                        if (currentContainer == firstC) {
                            firstC = currentContainer.next;
                        } else if (currentContainer == lastC) {
                            lastC = currentContainer.prev;
                        } else {
                            currentContainer.container = currentContainer.next.container;
                            currentContainer.next.container = null;
                        }
                    }

                    return true;
                }
            }
            currentContainer = currentContainer.next;
        }

        return false;
    }


    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        Container<E> currentContainer = lastC;

        while (currentContainer != null) {
            for (int i = currentContainer.size-1; i >=0; i--) {
                if (Objects.equals(currentContainer.container[i], o)) {
                    System.arraycopy(currentContainer.container, i + 1, currentContainer.container, i, currentContainer.size - i - 1);
                    currentContainer.container[currentContainer.size - 1] = null;
                    currentContainer.size--;

                    if (currentContainer.size == 0) {
                        if (currentContainer == firstC) {
                            firstC = currentContainer.next;
                        } else if (currentContainer == lastC) {
                            lastC = currentContainer.prev;
                        } else {
                            currentContainer.container = currentContainer.next.container;
                            currentContainer.next.container = null;
                        }
                    }

                    return true;
                }
            }
            currentContainer = currentContainer.prev;
        }
        return false;
    }



    @Override
    public boolean add(E e) {
        return offerLast(e);
    }

    @Override
    public boolean offer(E e) {
        return offerFirst(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) {
            addLast(element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return Deque.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Spliterator<E> spliterator() {
        return Deque.super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return Deque.super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return Deque.super.parallelStream();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
       return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        Container<E> currentContainer = firstC;

        while (currentContainer != null) {
            for (int i = 0; i < currentContainer.size; i++) {
                if (Objects.equals(currentContainer.container[i], o)) {
                    return true;
                }
            }

            currentContainer = currentContainer.next;
        }

        return false;
    }



    @Override
    public int size() {
        int count = 0;
        Container<E> currentContainer = firstC;

        while (currentContainer != null) {
            count += currentContainer.size;
            currentContainer = currentContainer.next;
        }

        return count;
    }

    @Override
    public boolean isEmpty() {
        return (firstC ==null);
    }

    private class TripletDequeIterator implements Iterator<E> {
        private Container<E> currentContainer;
        private int currentIndex;

        public TripletDequeIterator() {
            currentContainer = firstC;
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            if (currentContainer == null) {
                return false;
            }
            return currentContainer.next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            E element = (E) currentContainer.container[currentIndex];
            currentIndex++;

            if (currentIndex >= currentContainer.size) {
                currentContainer = currentContainer.next;
                currentIndex = 0;
            }

            return element;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new TripletDequeIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Deque.super.forEach(action);
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return Deque.super.toArray(generator);
    }
}