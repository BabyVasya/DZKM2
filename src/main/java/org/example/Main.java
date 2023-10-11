package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        TripletDeque<Integer> t = new TripletDeque<>();
        for (int i =0; i < 15; i++){
            t.addLast(i);
        }
        for (int i =0; i < 15; i++){
            t.addFirst(i);
        }
        t.removeLastOccurrence(3);
        t.remove(2);
    }
}