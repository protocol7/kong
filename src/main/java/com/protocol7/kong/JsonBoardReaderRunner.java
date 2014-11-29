package com.protocol7.kong;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.json.JSONObject;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.Files;

public class JsonBoardReaderRunner {

  public static void main(final String[] args) throws IOException {
    final String j = Files.toString(new File("bananas.json"), Charsets.UTF_8);

    final JsonBoardReader reader = new JsonBoardReader();
    final Board board = reader.read(new JSONObject(j));

    System.out.println(board);

    final Collection<Tile> tracks = board.getByType(TileType.SONG);
    final Tile monkey = board.findUnique(TileType.MONKEY);

    final BfsPathFinder bfs = new BfsPathFinder();

    final Optional<Paths> paths = bfs.findPaths(board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES), monkey, tracks);
    if (paths.isPresent()) {
      final Path shortest = paths.get().getShortest();
      System.out.println(shortest);
      System.out.println(shortest.getDirections());

      System.out.println(board.toStringWithPath(shortest));
    } else {
      System.err.println("No path found");
    }
  }
}
