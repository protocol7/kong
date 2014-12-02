package com.protocol7.kong;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class BfsPathFinder {

  public Path findPath(final Graph<Tile> graph, final Tile start, final Tile end) {
    final BreadthFirstSearch<Tile> bfs = new BreadthFirstSearch<>();
    return Path.fromNullable(bfs.search(graph, start, end));
  }


  public Optional<Paths> findPaths(final Graph<Tile> graph,
                                   final Tile start,
                                   final Collection<Tile> ends) {
    final BreadthFirstSearch<Tile> bfs = new BreadthFirstSearch<>();

    final List<Path> paths = Lists.newArrayList();
    for (final Tile end : ends) {
      paths.add(Path.fromNullable(bfs.search(graph, start, end)));
    }

    return Paths.fromNullables(paths);
  }

}
