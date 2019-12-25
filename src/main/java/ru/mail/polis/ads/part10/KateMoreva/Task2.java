package ru.mail.polis.ads.part10.KateMoreva;

import java.io.*;
import java.util.*;


public class Task2 {
    private static class Edge {
        int from;
        int to;
        int weight;

        Edge(int from, int to, int weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    static int [] prev;
 private static int find(int number) {
        if (number == prev[number]) {
            return number;
        }
     return prev[number] = find(prev[number]);

 }
    private static void union(int first, int second){
    first = find(first);
    second = find(second);
    if(first != second) {
        prev[second] = first;
    }
    }
    static class FastScanner {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        FastScanner(final InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
        }

        String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }


    public static void main(String[] args){
        final FastScanner in = new FastScanner(System.in);
        final int verticesCount = in.nextInt();
        final int edgesCount = in.nextInt();
        if (edgesCount == 0) {
            System.out.println(0);
            System.exit(0);
        }
        prev = new int[verticesCount + 1];
        for (int i = 1; i <= verticesCount; i++) {
            prev[i] = i;
        }
        Queue<Edge> queue = new PriorityQueue<>(verticesCount, Comparator.comparing(edge -> edge.weight));
        for (int i = 1; i <= edgesCount; ++i) {
            final int node1 = in.nextInt();
            final int node2 = in.nextInt();
            final int weight = in.nextInt();
            queue.add(new Edge(node1, node2, weight));
        }
        int min = 0;
        int counter = verticesCount;
        while (counter != 1) {
            Edge edge = queue.poll();

            if (find(edge.from) != find(edge.to)) {
                union(edge.from, edge.to);
                min += edge.weight;
                counter--;
            }
        }
            final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
            out.print(min);
            out.flush();
    }
}
