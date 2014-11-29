package com.protocol7.kong;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class SearchRunner2 {

  public static void main(final String[] args) throws IOException {
    search("src/test/resources/board6.txt");
  }

  private static void search(final String boardPath) throws IOException,
      FileNotFoundException {
    final BoardReader boardReader = new BoardReader();
    final Board board = boardReader.read(new FileReader(boardPath));

    final Tile monkey = board.findUnique(TileType.MONKEY);
    final Collection<Tile> tracks = board.getByType(TileType.SONG);
    final Graph<Tile> graph = board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES);

    final BfsPathFinder bfs = new BfsPathFinder();

    final Path shortestPath = bfs.findPaths(graph, monkey, tracks).get().getShortest();

    System.out.println(board.toStringWithPath(shortestPath));
  }

}
