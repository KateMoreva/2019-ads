package ru.mail.polis.ads.part10.KateMoreva;

import java.io.*;
import java.util.*;

public class Task4 {
    static void topologicalSort(int current, List<Integer>[] graph, boolean[] visited, ArrayList<Integer> sorted) {
        visited[current] = true;
        for (Integer integer: graph[current]) {
            if (!visited[integer]) {
                topologicalSort(integer, graph, visited, sorted);
            }
        }
        sorted.add(current);
    }

    private static Set<Integer>[] colorsComb;

    static void backwardDfs(int current, List<Integer>[] graph, boolean[] unvisited, int currentColor, int[] colors) {
        unvisited[current] = false;
        colors[current] = currentColor;
        for (Integer integer: graph[current]) {
            if (unvisited[integer]) {
                backwardDfs(integer, graph, unvisited, currentColor, colors);
            } else if (colors[integer] != currentColor) {
                colorsComb[currentColor].add(colors[integer]);
            }
        }
    }

    public static void main(String[] args) {
        final Scanner in = new Scanner(System.in);
        final int n = in.nextInt();
        final int m = in.nextInt();

        final List<Integer>[] graph = new List[n + 1];
        final List<Integer>[] graphT = new List[n + 1];
        colorsComb = new Set[n + 1];

        for (int i = 1; i <= n; ++i) {
            graph[i] = new ArrayList<>();
            graphT[i] = new ArrayList<>();
            colorsComb[i] = new TreeSet<>();
        }

        for (int i = 1; i <= m ; ++i) {
            final int first = in.nextInt();
            final int second = in.nextInt();
            graph[first].add(second);
            graphT[second].add(first);
        }

        final boolean[] flags = new boolean[n + 1];
        final ArrayList<Integer> sorted = new ArrayList<>(n + 1);
        for (int i = 1; i <= n; ++i) {
            if (!flags[i]) {
                topologicalSort(i, graph, flags, sorted);
            }
        }

        int counter = 0;
        int[] colors = new int[n + 1];
        for (int i = n - 1; i >= 0; --i) {
            if (flags[sorted.get(i)]) {
                ++counter;
                backwardDfs(sorted.get(i), graphT, flags, counter, colors);
            }
        }

        int result = 0;
        for (int i = 1; i <= n; ++i) {
            result += colorsComb[i].size();
        }
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.println(result);
        out.flush();
    }
}

