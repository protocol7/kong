package com.protocol7.kong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class BoardReader {

  public Board read(final Reader input) throws IOException {

    final List<String> lines = readFromFile(input);

    final List<List<TileType>> board = new ArrayList<List<TileType>>();
    for (final String line : lines) {
      board.add(parseLine(line));
    }

    return new Board(board);
  }

  private List<String> readFromFile(final Reader input) throws IOException {
    final BufferedReader br = new BufferedReader(input);

    final List<String> lines = Lists.newArrayList();
    String line = br.readLine();
    while (line != null) {
      lines.add(line);
      line = br.readLine();
    }
    return lines;
  }

  private List<TileType> parseLine(final String line) {
    final List<TileType> tiles = new ArrayList<TileType>();

    for (final char c : line.toCharArray()) {
      tiles.add(TileType.fromChar(c));
    }
    return tiles;
  }
}
