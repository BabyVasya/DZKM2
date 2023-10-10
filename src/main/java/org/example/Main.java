package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        TripletDeque<Integer> t = new TripletDeque<>();

        for (int i=0; i < 15 ;i++){
            t.addFirst(i);
        }
        t.addFirst(13);
        for (int i=0; i < 15 ;i++){
            t.addLast(i);
        }
        System.out.println(t.removeFirstOccurrence(14));
        System.out.println(t.getFirst());








    }
}