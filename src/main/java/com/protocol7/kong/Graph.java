package com.protocol7.kong;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Graph<T> {

  private final Multimap<T, T> edges = ArrayListMultimap.create();

  public void addEdge(final T start, final T end) {
    edges.put(start, end);
  }

  public Collection<T> getNeighbors(final T start) {
    return edges.get(start);
  }

  @Override
  public String toString() {
    return edges.toString();
  }
}
