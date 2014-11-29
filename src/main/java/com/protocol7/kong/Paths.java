package com.protocol7.kong;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class Paths {

  public static Optional<Paths> fromNullables(final List<Path> paths) {
    final List<Path> nonNull = Lists.newArrayList();
    for (final Path path : paths) {
      if (path != null) {
        nonNull.add(path);
      }
    }

    if (!nonNull.isEmpty()) {
      return Optional.of(new Paths(nonNull));
    } else {
      return Optional.absent();
    }
  }

  private final List<Path> paths;

  private Paths(final List<Path> paths) {
    this.paths = paths;
  }

  public List<Path> getPaths() {
    return paths;
  }

  public Path getShortest() {
    Path shortestPath = null;
    int minimalLength = Integer.MAX_VALUE;
    for (final Path path : paths) {
      if (path.length() < minimalLength) {
        shortestPath = path;
        minimalLength = path.length();
      }
    }

    return shortestPath;
  }
}