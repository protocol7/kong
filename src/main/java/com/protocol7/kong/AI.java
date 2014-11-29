package com.protocol7.kong;

import java.util.Collection;
import java.util.List;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class AI {

  public Command move(final GameState gameState) {
    // go to closest song
    final BfsPathFinder pathFinder = new BfsPathFinder();

    final Board board = gameState.getBoard();
    final Tile monkey = board.findUnique(TileType.MONKEY);

    System.out.println(gameState.getInventory() + " --- " + gameState.getInventorySize());
    System.out.println(gameState.getInventory().size() < gameState.getInventorySize());
    if (gameState.getInventory().size() < gameState.getInventorySize()) {
      System.out.println("Go fetch more collectibles");
      final Optional<Direction> direction = findClosestCollectible(board, monkey, pathFinder);

      return Command.moveOrIdle(direction);
    } else {
      System.out.println("Full, go to user");
      // inventory full, go to user
      final Collection<Tile> users = board.getByType(TileType.USER);
      final Graph<Tile> graph = board.toGraph(monkey, Board.NON_WALKABLE_USER);
      final Optional<Direction> direction = findClosest(graph, monkey, pathFinder, users);

      return Command.moveOrIdle(direction);
    }
  }

  private Optional<Direction> findClosestCollectible(final Board board,
                                                     final Tile monkey,
                                                     final BfsPathFinder pathFinder) {
    final List<Tile> collectibles = Lists.newArrayList();
    collectibles.addAll(board.getByType(TileType.SONG));
    collectibles.addAll(board.getByType(TileType.ALBUM));
    collectibles.addAll(board.getByType(TileType.PLAYLIST));

    return findClosest(board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES), monkey, pathFinder, collectibles);
  }

  private Optional<Direction> findClosest(final Graph<Tile> graph,
                                           final Tile monkey,
                                           final BfsPathFinder pathFinder,
                                           final Collection<Tile> targets) {
    final Optional<Paths> paths = pathFinder.findPaths(graph, monkey, targets);

    if (paths.isPresent()) {
      final Path shortest = paths.get().getShortest();
      return Optional.of(shortest.getFirstDirection());
    } else {
      return Optional.absent();
    }
  }



}