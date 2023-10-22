package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        TripletDeque<Integer> t = new TripletDeque<>(2000);

        for (int i =0; i < 999; i++){
            t.addFirst(i);
        }
        System.out.println(t.size());
        for (int i =0; i < 999; i++){
            t.addFirst(i);
        }
        System.out.println(t.size());
        for (int i =0; i < 3; i++){
            t.addFirst(i);
        }
        System.out.println(t.size());
    }
}