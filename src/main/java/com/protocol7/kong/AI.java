package com.protocol7.kong;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class AI {

  public Command doSomething(final GameState gameState) {
    // go to closest song
    final BfsPathFinder pathFinder = new BfsPathFinder();

    final Board board = gameState.getBoard();
    final Tile monkey = board.findUnique(TileType.MONKEY);

    if (gameState.getInventory().contains("banana")) {
      System.out.println("Using banana");
      return Command.use("banana");
    }

    // use speedy right away
    int turns;
    System.out.println("Speedy: " + gameState.getSpeedy());
    if (gameState.getSpeedy() > 0) {
      turns = 2;
    } else {
      turns = 1;
    }

    final Optional<List<Direction>> optDirections = nextMoves(gameState, pathFinder, board, monkey);

    final List<Direction> directions = optDirections.or(Collections.<Direction>emptyList())
        .stream()
        .limit(turns)
        .collect(Collectors.toList());

    if (!directions.isEmpty()) {
      return Command.move(directions);
    } else {
      return Command.idle();
    }
  }

  private Optional<List<Direction>> nextMoves(final GameState gameState,
                           final BfsPathFinder pathFinder,
                           final Board board,
                           final Tile monkey) {
    if (gameState.getInventory().size() < gameState.getInventorySize()) {
      System.out.println("Go fetch more collectibles");
      return findClosestCollectible(board, monkey, pathFinder);
    } else {
      System.out.println("Full, go to user");
      // inventory full, go to user
      final Collection<Tile> users = board.getByType(TileType.USER);
      final Graph<Tile> graph = board.toGraph(monkey, Board.NON_WALKABLE_USER);
      return findClosest(graph, monkey, pathFinder, users);
    }
  }

  private Optional<List<Direction>> findClosestCollectible(final Board board,
                                                     final Tile monkey,
                                                     final BfsPathFinder pathFinder) {
    final List<Tile> collectibles = Lists.newArrayList();
    collectibles.addAll(board.getByType(TileType.SONG));
    collectibles.addAll(board.getByType(TileType.ALBUM));
    collectibles.addAll(board.getByType(TileType.PLAYLIST));
    collectibles.addAll(board.getByType(TileType.BANANA));

    return findClosest(board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES), monkey, pathFinder, collectibles);
  }

  private Optional<List<Direction>> findClosest(final Graph<Tile> graph,
                                           final Tile monkey,
                                           final BfsPathFinder pathFinder,
                                           final Collection<Tile> targets) {
    final Optional<Paths> paths = pathFinder.findPaths(graph, monkey, targets);

    if (paths.isPresent()) {
      final Path shortest = paths.get().getShortest();
      return Optional.of(shortest.getDirections());
    } else {
      return Optional.absent();
    }
  }



}