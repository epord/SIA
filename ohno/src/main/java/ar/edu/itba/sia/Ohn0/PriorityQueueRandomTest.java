package ar.edu.itba.sia.Ohn0;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueRandomTest {

    public static class Box {
        int value;
        String name;

        public Box(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }
    public static void main(String[] args) {
        PriorityQueue<Box> pq = new PriorityQueue<>(Comparator.comparingInt((Box b) -> b.value).thenComparing(b -> Math.random() - 0.5));
        pq.add(new Box(5, "aaa"));
        pq.add(new Box(5, "bbb"));
        pq.add(new Box(5, "ccc"));
        pq.add(new Box(5, "ddd"));
        pq.add(new Box(5, "eee"));
        pq.add(new Box(5, "fff"));
        pq.add(new Box(5, "ggg"));
        pq.add(new Box(5, "hhh"));
        pq.add(new Box(5, "iii"));
        pq.add(new Box(5, "jjj"));
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
        System.out.println(pq.poll().name);
    }
}
