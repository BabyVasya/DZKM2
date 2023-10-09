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
        for (int i=0; i < 15 ;i++){
            t.addLast(i);
        }

        t.removeFirst();
        System.out.println(t.size());






    }
}