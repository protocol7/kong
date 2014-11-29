package com.protocol7.kong;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SearchRunner {

  public static void main(final String[] args) throws IOException {
    search("src/test/resources/board1.txt");
    search("src/test/resources/board2.txt");
    search("src/test/resources/board3.txt");
    search("src/test/resources/board4.txt");
    search("src/test/resources/board5.txt");
  }

  private static void search(final String path) throws IOException,
      FileNotFoundException {
    final BoardReader boardReader = new BoardReader();
    final Board board = boardReader.read(new FileReader(path));

    final Tile monkey = board.findUnique(TileType.MONKEY);
    final Tile track = board.findUnique(TileType.SONG);
    final Graph<Tile> graph = board.toGraph(monkey, Board.NON_WALKABLE_COLLECTIBLES);

    final BreadthFirstSearch<Tile> bfs = new BreadthFirstSearch<Tile>();

    System.out.println(board);
    System.out.println(bfs.search(graph, monkey, track));
  }
}
