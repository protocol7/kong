package com.protocol7.kong;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BreadthFirstSearch<T> {

  public List<T> search(final Graph<T> graph, final T start, final T end) {
    final Map<T, T> visited = Maps.newHashMap();
    final Queue<T> queue = new LinkedList<T>();

    queue.add(start);

    while (!queue.isEmpty()) {
      T next = queue.remove();

      if (next.equals(end)) {
        // we're done
        final List<T> parents = Lists.newArrayList();
        while (!next.equals(start)) {
          parents.add(next);
          next = visited.get(next);
        }
        parents.add(start);
        Collections.reverse(parents);
        return parents;
      }

      for (final T neighbor : graph.getNeighbors(next)) {
        if (!visited.containsKey(neighbor)) {
          queue.add(neighbor);
          visited.put(neighbor, next);
        }
      }
    }

    return null;
  }

  public static void main(final String[] args) {
    final Graph<String> g = new Graph<String>();
    g.addEdge("A", "B");
    g.addEdge("B", "A");
    g.addEdge("B", "C");
    g.addEdge("C", "D");

    System.out.println(new BreadthFirstSearch<String>().search(g, "A", "D"));
    System.out.println(new BreadthFirstSearch<String>().search(g, "A", "C"));
    System.out.println(new BreadthFirstSearch<String>().search(g, "A", "E"));
  }
}
