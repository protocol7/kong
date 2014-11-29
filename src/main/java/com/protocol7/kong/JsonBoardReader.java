package com.protocol7.kong;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonBoardReader {

  public Board read(final JSONObject json) {
    final JSONArray position = json.getJSONArray("position");
    final Pos pos = new Pos(position.getInt(1), position.getInt(0));
    final JSONArray layout = json.getJSONArray("layout");

    final List<List<TileType>> board = new ArrayList<List<TileType>>();
    for (int r = 0; r<layout.length(); r++) {
      final JSONArray row = layout.getJSONArray(r);
      board.add(parseLine(row, r, pos));
    }

    return new Board(board);
  }

  private List<TileType> parseLine(final JSONArray row, final int rowNumber, final Pos pos) {
    final List<TileType> tiles = new ArrayList<TileType>();

    for (int c = 0; c<row.length(); c++) {
      TileType type = TileType.fromWord(row.getString(c));
      if (type == TileType.MONKEY) {
        if (!new Pos(c, rowNumber).equals(pos)) {
          type = TileType.OTHER_MONKEY;
        }
      }
      tiles.add(type);
    }
    return tiles;
  }
}
