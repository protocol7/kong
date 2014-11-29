package com.protocol7.kong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathFinder implements PathFinder {

  public static class AStarTile {

    private final Tile tile;
    private AStarTile parent;
    private double cachedParentCost = 0.0;

    public AStarTile(final Tile tile) {
      this.tile = tile;
    }

    public AStarTile getParent() {
      return parent;
    }

    public void setParent(final AStarTile parent) {
      this.parent = parent;
    }

    private boolean isStart() {
      return parent == null;
    }

    public boolean isFree() {
      return tile.getType().isFree();
    }

    public double getLocalCost(final Tile goal) {
      if (isStart()) {
        return 0.0;
      }

      return 1.0 * (Math.abs(tile.getPos().getX() - goal.getPos().getX()) + Math.abs(tile.getPos()
          .getY() - goal.getPos().getY()));
    }

    public double getParentCost(final Tile goal) {

      if (isStart()) {
        return 0.0;
      }

      if (cachedParentCost == 0.0) {
        cachedParentCost = 1.0 + .5 * (parent.getParentCost(goal) - 1.0);
      }

      return cachedParentCost;
    }

    public double getPassThrough(final Tile goal) {
      if (isStart()) {
        return 0.0;
      }

      return getLocalCost(goal) + getParentCost(goal);
    }

    @Override
    public int hashCode() {
      return tile.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final AStarTile other = (AStarTile) obj;
      return other.tile.equals(tile);
    }

    @Override
    public String toString() {
      return tile.toString();
    }
  }

  private Set<AStarTile> getAdjacencies(final Board board, final Tile tile) {
    final List<Tile> adjacencies = board.getAdjacencies(tile);
    final Set<AStarTile> astarAjd = new HashSet<AStarTile>();
    for (final Tile ajd : adjacencies) {
      astarAjd.add(new AStarTile(ajd));
    }
    return astarAjd;
  }

  public Path findPath(final Board board, final Tile start, final Tile goal) {
    final AStarTile astarStart = new AStarTile(start);
    final AStarTile astarGoal = new AStarTile(goal);

    final PriorityQueue<AStarTile> opened = new PriorityQueue<AStarTile>(16, new Comparator<AStarTile>() {
      public int compare(final AStarTile o1, final AStarTile o2) {
        return (int) Math.round(o1.getPassThrough(goal) - o2.getPassThrough(goal));
      }
    });

    final Set<AStarTile> closed = new HashSet<AStarTile>();

    final Set<AStarTile> adjacencies = getAdjacencies(board, start);

    for (final AStarTile adjacency : adjacencies) {
      if (adjacency.isFree()) {
        adjacency.setParent(astarStart);
        if (adjacency.isStart() == false) {
          opened.add(adjacency);
        }
      }
    }

    while (!opened.isEmpty()) {
      final AStarTile best = opened.poll();
      closed.add(best);

      if (best.equals(astarGoal)) {
        final List<Tile> path = new ArrayList<Tile>();
        path.add(best.tile);
        AStarTile parent = best.parent;
        while (parent != null) {
          path.add(parent.tile);
          parent = parent.parent;
        }

        Collections.reverse(path);
        return new Path(path);
      } else {
        final Set<AStarTile> neighbors = getAdjacencies(board, best.tile);
        for (final AStarTile neighbor : neighbors) {
          if (neighbor.isFree()) {

            if (opened.contains(neighbor) || closed.contains(neighbor)) {
              final AStarTile tmp = new AStarTile(neighbor.tile);
              tmp.setParent(best);
              if (tmp.getPassThrough(goal) >= neighbor.getPassThrough(goal)) {
                continue;
              }
            }

            neighbor.setParent(best);

            opened.remove(neighbor);
            closed.remove(neighbor);
            opened.add(neighbor);
          }
        }
      }
    }

    return null;
  }
}
