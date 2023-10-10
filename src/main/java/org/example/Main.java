package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        TripletDeque<Integer> t = new TripletDeque<>();
//        for (int i = 0; i < 15; i++) {
//            t.addFirst(i);
//        }
//        for (int i = 0; i < 15; i++) {
//            t.addLast(-i);
//        }
//        t.removeFirstOccurrence(3);
//        t.removeLastOccurrence(-12);
//        t.remove(1);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(null);
        list.add(3);
        list.add(null);
        list.add(5);

        // Отфильтровать null элементы
        ArrayList<Integer> filteredList = (ArrayList<Integer>) list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println(filteredList); // Выведет: [1, 3, 5]
    }
}