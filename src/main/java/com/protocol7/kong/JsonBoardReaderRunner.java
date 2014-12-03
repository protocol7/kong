package com.protocol7.kong;

import java.io.File;
import java.io.IOException;
import org.json.JSONObject;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class JsonBoardReaderRunner {

  public static void main(final String[] args) throws IOException {
    //for (int i = 149; i>0; i--) {
    //  run("tunnels-" + i + ".json");
    //}
    run("tunnels-1.json");
  }

  private static void run(final String path) throws IOException {
    final String j = Files.toString(new File(path), Charsets.UTF_8);

    final JsonBoardReader reader = new JsonBoardReader();
//    final Board board = reader.read(new JSONObject(j));
//
//    final Collection<Tile> tracks = board.getByType(TileType.SONG);
//    final Tile monkey = board.findUnique(TileType.MONKEY);
//
//    final BfsPathFinder bfs = new BfsPathFinder();
//
//    final Optional<Paths> paths = bfs.findPaths(board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES), monkey, tracks);
//    if (paths.isPresent()) {
//      final Path shortest = paths.get().getShortest();
//      System.out.println(shortest);
//      System.out.println(shortest.getDirections());
//
//      System.out.println(board.toStringWithPath(shortest));
//    } else {
//      System.err.println("No path found");
//    }

    final AI ai = new AI();
    System.out.println(ai.doSomething(GameState.fromJson(new JSONObject(j))));
  }
}
