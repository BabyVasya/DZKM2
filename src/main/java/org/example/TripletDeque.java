package org.example;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class TripletDeque<E> implements Deque<E>, Containerable {
    public static final int CONTAINER_SIZE = 5;

    private class Container<E> {
        private E[] container;
        private Container<E> next;
        private Container<E> prev;
        private int size;

        public Container() {
            this.container = (E[]) new Object[CONTAINER_SIZE];
        }
    }
    private List<Container<E>> listOfContainers;
    private int sizeOfList = 1000;
    public TripletDeque() {
        this.listOfContainers = new ArrayList<>(Collections.nCopies(this.sizeOfList, null));
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        Container<E> cont = listOfContainers.get(cIndex);
        if(listOfContainers.get(cIndex) == null) {
            return null;
        }
        return cont.container;
    }
    private int getNullContainerIndexIterateFromTheFirstForAddFirst() {
        for (int i = 0; i < listOfContainers.size()-1 ; i++) {
            if (listOfContainers.get(i) == null && listOfContainers.get(i+1) ==null ) {
                return i;
            }
        }
        return -1;
    }


    private int getFirstNonNullContainerIndexIterateFromTheEnd() {
        for (int i = listOfContainers.size()-1; i >=0 ; i--) {
            if (listOfContainers.get(i) != null ) {
                return i;
            }
        }
        return -1;
    }

    private int getFirstNullInFirstContainer(E[] container) {
        for (int i = CONTAINER_SIZE-1; i>=0 ; i--) {
            if (container[i] == null ) {
                return i;
            }
        }
        return -1;
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

    private int getFirstNonNullInContainerForGetLast0(E[] container) {
        for (int i = CONTAINER_SIZE-1;i>=0 ; i--) {
            if (i == 0) {
                if(container[i] != null) {
                    return i;
                }
            }
            if (container[i] != null) {
                return i;
            }
        }
        return -1;
    }
    private int getFirstNonNullInContainerForGetLast1(E[] container) {
        for (int i = 0; i<=CONTAINER_SIZE-1; i++) {
            if (container[i] != null && container[i+1] !=null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addFirst(E e) {
        if(e == null) {
            throw new NullPointerException();
        }
        if(isEmpty()) {
            Container<E> newContainer = new Container<>();
            listOfContainers.set(0,newContainer);
            listOfContainers.get(0).size = 0;
        }
        if(listOfContainers.get(0).size == CONTAINER_SIZE) {
            Container<E> newContainer = new Container<>();
            Collections.rotate(listOfContainers.subList(0, Collections.lastIndexOfSubList(listOfContainers, Collections.singletonList(null)) + 1), 1);
            listOfContainers.set(0, newContainer);
            listOfContainers.get(0).next = listOfContainers.get(1);
            listOfContainers.get(1).prev = listOfContainers.get(0);
        }
        E[] current = (E[]) getContainerByIndex(0);
        int i = getFirstNullInFirstContainer(current);
        current[i] = e;
        listOfContainers.get(0).size++;
    }


    @Override
    public void addLast(E e) {
        if(e == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            Container<E> newContainer = new Container<>();
            listOfContainers.set(0, newContainer);
        }
        int i = getFirstNonNullContainerIndexIterateFromTheEnd();
        if ( (listOfContainers.get(i).size == CONTAINER_SIZE) ) {
            Container<E> newContainer = new Container<>();
            listOfContainers.set(i + 1, newContainer);
            listOfContainers.get(i + 1).prev = listOfContainers.get(i);
            listOfContainers.get(i).next = listOfContainers.get(i+1);
            listOfContainers.get(i + 1).container[listOfContainers.get(i + 1).size]=e;
            listOfContainers.get(i + 1).size++;

        }

        if ( (listOfContainers.get(i).size <CONTAINER_SIZE) ) {
            Integer[] busy = new Integer[5];
            int putHere =0;
            if(!isContainerEmpty(i)) {
                for (int j =0; j <= CONTAINER_SIZE-1; j++) {
                    if(listOfContainers.get(i).container[j] != null){
                        if(j ==0) busy[0]=j;
                        if(j ==1) busy[1]=j;
                        if(j ==2) busy[2]=j;
                        if(j ==3) busy[3]=j;
                        if(j ==4) busy[4]=j;
                    }
                }

                for (int k = 4; k >= 0; k--) {
                    if(busy[k] ==null) {
                        putHere = k;
                    }
                }
            }
            listOfContainers.get(i).container[putHere] = e;
            listOfContainers.get(i).size++;
        }
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
        listOfContainers.get(0).container[getFirstNonNullInFirstContainerForGetFirst(listOfContainers.get(0).container)] = null;
        listOfContainers.get(0).size--;
        if(isContainerEmpty(0)) {
            listOfContainers.set(0, null);
            int i = getNullContainerIndexIterateFromTheFirstForAddFirst();
            for (int j = 0; j < i; j++) {
                listOfContainers.set(j, listOfContainers.get(j + 1));
            }
        }
        return removing;
    }

    public boolean isContainerEmpty(int i) {
        int allMatch = 0;
        for(int j = 0; j <= CONTAINER_SIZE-1; j++) {
            if (listOfContainers.get(i).container[j] == null){
                allMatch++;
            }
        }
        if(allMatch == 5) {
            return true;
        }
        return false;

    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E removing = getLast() ;
        int i = getFirstNonNullContainerIndexIterateFromTheEnd();
        listOfContainers.get(i).container[getFirstNonNullInLastContainerForRemoveLast(listOfContainers.get(i).container)] = null;
        listOfContainers.get(i).size--;
        if(isContainerEmpty(i)) {
            listOfContainers.set(i, null);
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
        if (isEmpty() ) throw new NoSuchElementException("Очередь пуста");
        if(listOfContainers.get(0).size == CONTAINER_SIZE) {
            E[] cont = (E[]) getContainerByIndex(0);
            return cont[0];
        }
        return listOfContainers.get(0).container[getFirstNonNullInFirstContainerForGetFirst(listOfContainers.get(0).container)];
    }

    @Override
    public E getLast() {
        if (isEmpty() ) throw new NoSuchElementException("Очередь пуста");
        int nonNullCont = getFirstNonNullContainerIndexIterateFromTheEnd();
        if(listOfContainers.get(1) == null) {
            E[] cont = (E[]) getContainerByIndex(nonNullCont);
            return cont[getFirstNonNullInContainerForGetLast0(cont)];
        }
        if(listOfContainers.get(nonNullCont).size == CONTAINER_SIZE && listOfContainers.get(0).size < CONTAINER_SIZE) {
            E[] cont = (E[]) getContainerByIndex(nonNullCont);
            return cont[getFirstNonNullInContainerForGetLast1(listOfContainers.get(nonNullCont).container)];
        }
        if(listOfContainers.get(nonNullCont).size == CONTAINER_SIZE ) {
            E[] cont = (E[]) getContainerByIndex(nonNullCont);
            return cont[CONTAINER_SIZE-1];
        }
        E[] cont = (E[]) getContainerByIndex(nonNullCont);
        return cont[getFirstNonNullInContainerForGetLast0(cont)];
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
        if (isEmpty()) {
            return false;
        }
        boolean result = false;
        for (int i = 0; i < listOfContainers.size()-1; i++) {
            if (listOfContainers.get(i) != null) {
                for (int j = 0; j < CONTAINER_SIZE; j++) {
                    if (listOfContainers.get(i).container[j] != null && listOfContainers.get(i).container[j].equals(o)) {
                        listOfContainers.get(i).container[j] = null;
                        listOfContainers.get(i).size--;
                        shiftElementsWithinContainer(i);
                        if(isContainerEmpty(i)) {
                            listOfContainers.set(i, null);
                            if(listOfContainers.get(i+1) != null) {
                                Container<E> tmp = listOfContainers.get(i+1);
                                listOfContainers.set(i+1, null);
                                listOfContainers.set(i, tmp);
                            }
                        }
                        result = true;
                        break;
                    }
                }
                if (result) {
                    break;
                }
            }
        }
        return result;
    }
    private void shiftElementsWithinContainer(int i) {
        if (isEmpty()) return;
        for (int j = 0; j < CONTAINER_SIZE - 1; j++) {
            if (listOfContainers.get(i).container[j] == null && listOfContainers.get(i).container[j + 1] != null) {
                for (int k = j; k < CONTAINER_SIZE - 1; k++) {
                    listOfContainers.get(i).container[k] = listOfContainers.get(i).container[k + 1];
                }
                listOfContainers.get(i).container[CONTAINER_SIZE - 1] = null;
            }
        }
    }


    @Override
    public boolean removeLastOccurrence(Object o) {
        if (isEmpty()) {
            return false;
        }
        boolean result = false;
        for (int i = listOfContainers.size()-1; i >=0; i--) {
            if (listOfContainers.get(i) != null) {
                for (int j = CONTAINER_SIZE-1; j >=0; j--) {
                    if (listOfContainers.get(i).container[j] != null && listOfContainers.get(i).container[j].equals(o)) {
                        listOfContainers.get(i).container[j] = null;
                        listOfContainers.get(i).size--;
                        shiftElementsWithinContainer(i);
                        if(isContainerEmpty(i)) {
                            listOfContainers.set(i, null);
                            if (i > 0 && listOfContainers.get(i - 1) != null) {
                                Container<E> tmp = listOfContainers.get(i - 1);
                                listOfContainers.set(i - 1, null);
                                listOfContainers.set(i, tmp);
                            }
                        }
                        result = true;
                        break;
                    }
                }
                if (result) {
                    break;
                }
            }
        }
        return result;
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
        listOfContainers.stream().forEach(el -> listOfContainers.set(listOfContainers.indexOf(el), null));
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
        boolean result = false;
            for(int i =0; i < listOfContainers.size()-1; i++) {
                if(listOfContainers.get(i) != null) {
                    for(int j = 0; j< CONTAINER_SIZE; j++ ) {
                        if (listOfContainers.get(i).container[CONTAINER_SIZE - 1 - j] != null && (listOfContainers.get(i).container[CONTAINER_SIZE - 1 - j].equals(o))  ) {
                            result = true;
                            break;
                        }
                    }
                    if(result) break;
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
        private int currentIndex;
        private Container<E> currentContainer;

        public CustomIterator() {
            currentIndex = 0;
            currentContainer = listOfContainers.get(0);
        }

        @Override
        public boolean hasNext() {
            // Если текущий контейнер не равен null и текущий индекс меньше размера контейнера,
            // то есть следующий элемент.
            return currentContainer != null && currentIndex < currentContainer.size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = currentContainer.container[currentIndex];
            currentIndex++;

            if (currentIndex == currentContainer.size) {
                currentIndex = 0;
                currentContainer = currentContainer.next;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return Deque.super.toArray(generator);
    }


}