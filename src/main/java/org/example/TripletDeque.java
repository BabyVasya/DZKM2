package org.example;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TripletDeque<E> implements Deque<E>, Containerable {
    public static final int CONTAINER_SIZE = 5;

    private class Container<E> {
        private List<E> container;
        private int size;
        private Container<E> next;
        private Container<E> prev;

        public Container() {
            this.container = new ArrayList<>(Collections.nCopies(CONTAINER_SIZE, null));
            this.size = 0;
        }
    }

    private List<Container<E>> listOfContainers;
    private int sizeOfList = 1000;
    public TripletDeque() {
        this.listOfContainers = new ArrayList<>(Collections.nCopies(this.sizeOfList, null));
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        return new Container[]{listOfContainers.get(cIndex)};
    }

    @Override
    public void addFirst(E e) {
        if(e == null) {
            throw new NullPointerException();
        }
        if(isEmpty()) {
            Container<E> newContainer = new Container<>();
            listOfContainers.set(0, newContainer);
        }
        if(listOfContainers.get(0).size == CONTAINER_SIZE) {
            Container<E> newContainer = new Container<>();
            Collections.rotate(listOfContainers.subList(0, Collections.lastIndexOfSubList(listOfContainers, Collections.singletonList(null)) + 1), 1);
            listOfContainers.set(0, newContainer);
            listOfContainers.get(0).next = listOfContainers.get(1);
            listOfContainers.get(1).prev = listOfContainers.get(0);
        }
        listOfContainers.get(0).container.set(CONTAINER_SIZE - 1 - listOfContainers.get(0).size, e);
        listOfContainers.get(0).size++;

    }

    @Override
    public void addLast(E e) {
        if(e == null) {
            throw new NullPointerException();
        }
        createNewContainer();
        int i = getLastNonNullContainerIndex();
        if ( (listOfContainers.get(i).size == CONTAINER_SIZE) ) {
            Container<E> newContainer = new Container<>();
            listOfContainers.set(i + 1, newContainer);
            listOfContainers.get(i + 1).prev = listOfContainers.get(i);
            listOfContainers.get(i).next = listOfContainers.get(i+1);
            listOfContainers.get(i + 1).container.set(listOfContainers.get(i + 1).size, e);
            listOfContainers.get(i + 1).size++;

        }
        if ( (listOfContainers.get(i).size <CONTAINER_SIZE) ) {
            listOfContainers.get(i).container.set(listOfContainers.get(i).size, e);
            listOfContainers.get(i).size++;
        }

    }

    private int getLastNonNullContainerIndex() {
        for (int i = listOfContainers.size() - 1; i >= 0; i--) {
            if (listOfContainers.get(i) != null) {
                return i;
            }
        }
        return -1;
    }

    private void createNewContainer() {
        if(isEmpty()) {
            Container<E> newContainer = new Container<>();
            listOfContainers.set(0, newContainer);
        }
    }


    @Override
    public boolean offerFirst(E e) {
         addFirst(e);
         return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if(isEmpty()) throw new NoSuchElementException("Такого элемента нет");
        E removing;
        if(listOfContainers.get(0).container.get(CONTAINER_SIZE - listOfContainers.get(0).size) == null) {
            removing = listOfContainers.get(0).container.set(listOfContainers.get(0).size - 1 , null);
        } else {
            removing = listOfContainers.get(0).container.set(CONTAINER_SIZE - listOfContainers.get(0).size, null);
        }

        listOfContainers.get(0).size--;
        if(listOfContainers.get(0).container.stream().allMatch(el -> el ==null)) listOfContainers.set(0, null);
        shiftNonNullElementsToFront();
        return  removing;
    }

    private void shiftNonNullElementsToFront() {
        boolean firstNullFound = false;
        for (int i = 0; i < listOfContainers.size(); i++) {
            if (listOfContainers.get(i) == null && !firstNullFound) {
                firstNullFound = true;
            } else if (listOfContainers.get(i) != null && firstNullFound) {
                Container<E> temp = listOfContainers.get(i);
                listOfContainers.set(i, null);
                listOfContainers.set(i - 1, temp);
            }
        }
    }
    @Override
    public E removeLast() {
        if(isEmpty()) throw new NoSuchElementException("Такого элемента нет");
        int i = getLastNonNullContainerIndex();
        int j = getFirstNotNullFromEndInTheContainer(i);
        E removing = listOfContainers.get(i).container.set(j, null);
        listOfContainers.get(i).size--;
        if(listOfContainers.get(i).container.stream().allMatch(el -> el ==null)) listOfContainers.set(i, null);
        shiftNonNullElementsToFront();
        return removing;
    }
    private int getFirstNotNullFromEndInTheContainer(int i) {
        for (int j = listOfContainers.get(i).container.size() - 1; j >= 0; j--) {
            if (listOfContainers.get(i).container.get(j) != null) {
                return j;
            }
        }
        return -1;
    }

    @Override
    public E pollFirst() {
        if(listOfContainers.get(0).container.get(CONTAINER_SIZE - listOfContainers.get(0).size) == null || listOfContainers.isEmpty()) return null;
        E removing =  listOfContainers.get(0).container.set(CONTAINER_SIZE - listOfContainers.get(0).size, null);
        if(listOfContainers.get(0).container.stream().allMatch(el -> el ==null)) listOfContainers.set(0, null);
        return removing;
    }

    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E getFirst() {
        if (isEmpty() ) throw new NoSuchElementException("Очередь пуста");
        return listOfContainers.get(0).container.get(CONTAINER_SIZE - listOfContainers.get(0).size);
    }

    @Override
    public E getLast() {
        if (isEmpty() ) throw new NoSuchElementException("Очередь пуста");
        E result;
        int i = getLastNonNullContainerIndex();
        E tmp1 = listOfContainers.get(i).container.get(CONTAINER_SIZE-1);
        E tmp2 = listOfContainers.get(i).container.get(0);
        if(tmp1 == null) {
            return tmp2;
        } else return tmp1;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
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

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        if(isEmpty()) {
            throw new NoSuchElementException("Очередь пуста, удалять нечего");
        }
        boolean flag = false;
        boolean result = false;
            for(int i =0; i < listOfContainers.size()-1; i++) {
                for(int j = 0; j< CONTAINER_SIZE; j++ ) {
                    if ( (listOfContainers.get(i).container.get(CONTAINER_SIZE - 1 - j).equals(o))  ) {
                        listOfContainers.get(i).container.set(CONTAINER_SIZE - 1 - j, null);
                        listOfContainers.get(i).size--;
                        if(listOfContainers.get(i).container.stream().allMatch(el -> el ==null)) {
                            listOfContainers.set(i, null);
                        }
                        Collections.sort(listOfContainers.get(i).container, Comparator.comparing(element -> element == null ? 0 : 1));
                        result = true;
                        break;
                    }
                }
                if(result == true) break;
            }
        return flag;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {

        boolean result = false;
            for(int i =0; i < listOfContainers.size()-1; i++) {
                if(listOfContainers.get(i) != null) {
                    for(int j = 0; j< CONTAINER_SIZE; j++ ) {
                        if (listOfContainers.get(i).container.get(CONTAINER_SIZE - 1 - j) != null && (listOfContainers.get(i).container.get(CONTAINER_SIZE - 1 - j).equals(o))  ) {
                            result = true;
                            break;
                        }
                    }
                    if(result == true) break;
                } else if (listOfContainers.get(i) == null) {
                    return result;
                }
            }
        return result;
    }

    @Override
    public int size() {
        int counter = 0;
        for (int i = listOfContainers.size() - 1; i >= 0; i--) {
            if (listOfContainers.get(i) != null) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public boolean isEmpty() {
        return listOfContainers.stream().allMatch(elements -> elements == null);
    }

    private class CustomIterator implements Iterator<E> {
        private int containerIndex;  // Индекс текущего контейнера
        private int elementIndex;    // Индекс текущего элемента в контейнере

        public CustomIterator() {
            containerIndex = 0;
            elementIndex = 0;
        }

        @Override
        public boolean hasNext() {
            while (containerIndex < listOfContainers.size() &&
                    (listOfContainers.get(containerIndex) == null ||
                            (elementIndex >= CONTAINER_SIZE && listOfContainers.get(containerIndex).next == null))) {
                containerIndex++;
                elementIndex = 0;
            }
            return containerIndex < listOfContainers.size();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            E element = listOfContainers.get(containerIndex).container.get(elementIndex);
            elementIndex++;

            if (elementIndex >= CONTAINER_SIZE) {
                containerIndex++;
                elementIndex = 0;
            }

            return element;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomIterator();
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
        return null;
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return Deque.super.toArray(generator);
    }


}