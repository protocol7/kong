package com.protocol7.kong;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class Board {

  public static final Set<TileType> NON_WALKABLE_COLLECTIBLES = ImmutableSet.of(TileType.WALL, TileType.OTHER_MONKEY, TileType.USER);
  public static final Set<TileType> NON_WALKABLE_USER = ImmutableSet.of(TileType.WALL, TileType.OTHER_MONKEY);


  private final List<List<Tile>> board;
  private final Multimap<TileType, Tile> byType = ArrayListMultimap.create();

  public Board(final List<List<TileType>> board) {
    checkNotNull(board);
    checkArgument(board.size() != 0);
    final int width = board.get(0).size();
    checkArgument(width != 0);
    for (final List<TileType> line : board) {
      checkArgument(width == line.size());
    }

    final List<List<Tile>> tileBoard = Lists.newArrayList();
    for (int y = 0; y < board.size(); y++) {
      final List<TileType> line = board.get(y);
      final List<Tile> tileLine = Lists.newArrayList();
      for (int x = 0; x < line.size(); x++) {
        final TileType type = line.get(x);
        final Tile tile = new Tile(type, new Pos(x, y));
        tileLine.add(tile);
        byType.put(type, tile);
      }
      tileBoard.add(tileLine);
    }

    this.board = tileBoard;
  }

  public Tile getTile(final int x, final int y) {
    return board.get(y).get(x);
  }

  public Tile getTile(final Pos pos) {
    return getTile(pos.getX(), pos.getY());
  }

  public Collection<Tile> getByType(final TileType type) {
    final Collection<Tile> result = byType.get(type);
    if (result != null) {
      return result;
    } else {
      return Collections.emptyList();
    }
  }

  public EnumMap<Direction, Tile> getAdjacencies(final Tile tile) {
    final EnumMap<Direction, Tile> adj = Maps.newEnumMap(Direction.class);

    {
      final int x = tile.getPos().getX() - 1;
      final int y = tile.getPos().getY();
      if (inBoard(x, y)) {
        adj.put(Direction.LEFT, getTile(x, y));
      }
    }
    {
      final int x = tile.getPos().getX() + 1;
      final int y = tile.getPos().getY();
      if (inBoard(x, y)) {
        adj.put(Direction.RIGHT, getTile(x, y));
      }
    }
    {
      final int x = tile.getPos().getX();
      final int y = tile.getPos().getY() - 1;
      if (inBoard(x, y)) {
        adj.put(Direction.UP, getTile(x, y));
      }
    }
    {
      final int x = tile.getPos().getX();
      final int y = tile.getPos().getY() + 1;
      if (inBoard(x, y)) {
        adj.put(Direction.DOWN, getTile(x, y));
      }
    }

    return adj;
  }

  private boolean inBoard(final int x, final int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  public Graph<Tile> toGraph(final Tile start, final Set<TileType> nonWalkables) {
    final Set<Tile> added = new HashSet<Tile>();
    final Queue<Tile> tiles = new LinkedList<Tile>();
    final Graph<Tile> graph = new Graph<Tile>();

    tiles.add(start);
    added.add(start);

    while (!tiles.isEmpty()) {
      final Tile next = tiles.remove();

      final Collection<Tile> adjacents = getAdjacencies(next).values();

      for (final Tile adjacent : adjacents) {
        if (!added.contains(adjacent) && !nonWalkables.contains(adjacent.getType())) {
          tiles.add(adjacent);
          graph.addEdge(next, adjacent);
          added.add(adjacent);
        }
      }
    }
    return graph;
  }

  public Tile findUnique(final TileType type) {
    final Collection<Tile> tiles = getByType(type);

    if (!tiles.isEmpty()) {
      return tiles.iterator().next();
    } else {
      return null;
    }
  }

  public int getWidth() {
    return board.get(0).size();
  }

  public int getHeight() {
    return board.size();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for (final List<Tile> line : board) {
      for (final Tile tile : line) {
        sb.append(tile.getType().getChar());
      }
      sb.append('\n');
    }
    return sb.deleteCharAt(sb.length() - 1).toString();
  }

  public String toStringWithPath(final Path path) {
    final StringBuilder sb = new StringBuilder();
    for (final List<Tile> line : board) {
      for (final Tile tile : line) {
        if (path.contains(tile) && tile.getType() == TileType.EMPTY) {
          sb.append('*');
        } else {
          sb.append(tile.getType().getChar());
        }
      }
      sb.append('\n');
    }
    return sb.deleteCharAt(sb.length() - 1).toString();
  }
}
