package com.protocol7.kong;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
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

    // use bananas if we're not already speedy
    if (gameState.getInventory().contains("banana") && gameState.getSpeedy() == 0) {
      System.out.println("Using banana");
      return Command.use("banana");
    } else if (gameState.getInventory().contains("trap") && goodPlaceForTrap(board, monkey)) {
      System.out.println("Using trap");
      return Command.use("trap");
    }

    // naively use speedy right away
    int turns;
    if (gameState.getSpeedy() > 0) {
      turns = 2;
    } else {
      turns = 1;
    }

    final Optional<Path> path = nextMoves(gameState, pathFinder, board, monkey);

    final List<Direction> directions;
    if (path.isPresent()) {
      directions = path.get().getDirections()
        .stream()
        .limit(turns)
        .collect(Collectors.toList());
    } else {
      directions = Collections.emptyList();
    }

    if (!directions.isEmpty()) {
      return Command.move(directions);
    } else {
      return Command.idle();
    }
  }

  private Optional<Path> nextMoves(final GameState gameState,
                           final BfsPathFinder pathFinder,
                           final Board board,
                           final Tile monkey) {
    final Optional<Path> closestUser = findClosestUser(pathFinder, board, monkey);
    final List<String> inventory = gameState.getInventory();
    if (// are we right by the user with stuff to leave?
        isNextToUser(closestUser, inventory) ||
        // is our inventory full?
        inventory.size() >= gameState.getInventorySize() ||
        // start going back to user if we're at the end of the game and we got stuff
        (closestUser.isPresent() && !gameState.getInventory().isEmpty() && closestUser.get().length() >= gameState.getRemainingTurns())) {
      System.out.println("Full or end game, go to user");
      // inventory full, go to user
      return closestUser;
    } else {
      System.out.println("Go fetch more collectibles");
      return findClosestCollectible(board, monkey, pathFinder);
    }
  }

  private boolean isNextToUser(final Optional<Path> path, final List<String> inventory) {
    return gotCollectible(inventory) && path.isPresent() && path.get().length() == 1;
  }

  private boolean gotCollectible(final List<String> inventory) {
    return inventory.contains(TileType.SONG.toString()) ||
        inventory.contains(TileType.ALBUM.toString()) ||
        inventory.contains(TileType.PLAYLIST.toString());
  }

  private Optional<Path> findClosestUser(final BfsPathFinder pathFinder,
                                                    final Board board,
                                                    final Tile monkey) {
    final Collection<Tile> users = board.getByType(TileType.USER);
    final Graph<Tile> graph = board.toGraph(monkey, Board.NON_WALKABLE_USER);
    return findClosest(graph, monkey, pathFinder, users);
  }

  private Optional<Path> findClosestCollectible(final Board board,
                                                     final Tile monkey,
                                                     final BfsPathFinder pathFinder) {
    final List<Tile> collectibles = Lists.newArrayList();
    collectibles.addAll(board.getByType(TileType.SONG));
    collectibles.addAll(board.getByType(TileType.ALBUM));
    collectibles.addAll(board.getByType(TileType.PLAYLIST));
    collectibles.addAll(board.getByType(TileType.BANANA));
    collectibles.addAll(board.getByType(TileType.TRAP));

    return findClosest(board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES), monkey, pathFinder, collectibles);
  }

  private Optional<Path> findClosest(final Graph<Tile> graph,
                                           final Tile monkey,
                                           final BfsPathFinder pathFinder,
                                           final Collection<Tile> targets) {
    final Optional<Paths> paths = pathFinder.findPaths(graph, monkey, targets);

    if (paths.isPresent()) {
      return Optional.of(paths.get().getShortest());
    } else {
      return Optional.absent();
    }
  }

  private boolean goodPlaceForTrap(final Board board, final Tile current) {
    final EnumMap<Direction, Tile> adj = board.getAdjacencies(current);

    return (isWall(adj.get(Direction.LEFT)) && isWall(adj.get(Direction.RIGHT)) && !isWall(adj.get(Direction.DOWN)) && !isWall(adj.get(Direction.UP)))
      || (!isWall(adj.get(Direction.LEFT)) && !isWall(adj.get(Direction.RIGHT)) && isWall(adj.get(Direction.DOWN)) && isWall(adj.get(Direction.UP)));
  }

  private boolean isWall(final Tile tile) {
    return tile != null && tile.getType() == TileType.WALL;
  }


}