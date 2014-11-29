package com.protocol7.kong;

import java.util.List;

import com.google.common.collect.Lists;

public class Path {

  public static Path fromNullable(final List<Tile> path) {
    if (path != null) {
      return new Path(path);
    } else {
      return null;
    }
  }

  private final List<Tile> path;

  public Path(final List<Tile> path) {
    this.path = path;
  }

  public List<Tile> getPath() {
    return path;
  }

  public int length() {
    return path.size();
  }

  public boolean contains(final Tile tile) {
    return path.contains(tile);
  }

  public List<Direction> getDirections() {
    final List<Direction> out = Lists.newArrayList();

    Tile last = null;
    for (final Tile tile : path) {
      if (last != null) {
        final int dx = tile.getPos().getX() - last.getPos().getX();
        final int dy = tile.getPos().getY() - last.getPos().getY();

        if (dx == -1 && dy == 0) {
          out.add(Direction.LEFT);
        } else if (dx == 1 && dy == 0) {
          out.add(Direction.RIGHT);
        } else if (dx == 0 && dy == -1) {
          out.add(Direction.UP);
        } else if (dx == 0 && dy == 1) {
          out.add(Direction.DOWN);
        } else {
          throw new IllegalStateException("Something wack!");
        }
      }

      last = tile;
    }

    return out;
  }

  public Direction getFirstDirection() {
    return getDirections().get(0);
  }

  @Override
  public String toString() {
    return path.toString();
  }


}