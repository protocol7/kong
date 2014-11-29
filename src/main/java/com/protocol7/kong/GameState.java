package com.protocol7.kong;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Lists;

public class GameState {

  public static GameState fromJson(final JSONObject json) {
    final JsonBoardReader boardReader = new JsonBoardReader();
    final Board board = boardReader.read(json);
    final List<String> inventory = arrayToList(json.getJSONArray("inventory"));
    final JSONObject buffs = json.getJSONObject("buffs");
    final int speedy = buffs.optInt("speedy", 0);
    final int inventorySize = json.getInt("inventorySize");
    final int remainingTurns = json.getInt("remainingTurns");

    return new GameState(board, speedy, inventory, inventorySize, remainingTurns);
  }

  private static List<String> arrayToList(final JSONArray a) {
    final List<String> out = Lists.newArrayList();
    for (int i = 0; i<a.length(); i++) {
      out.add(a.getString(i));
    }
    return out;
  }


  private final Board board;
  private final int speedy;
  private final List<String> inventory;
  private final int inventorySize;
  private final int remainingTurns;

  public GameState(
      final Board board,
      final int speedy,
      final List<String> inventory,
      final int inventorySize,
      final int remainingTurns) {
    this.board = board;
    this.speedy = speedy;
    this.inventory = inventory;
    this.inventorySize = inventorySize;
    this.remainingTurns = remainingTurns;
  }

  public Board getBoard() {
    return board;
  }

  public int getSpeedy() {
    return speedy;
  }

  public List<String> getInventory() {
    return inventory;
  }

  public int getInventorySize() {
    return inventorySize;
  }

  public int getRemainingTurns() {
    return remainingTurns;
  }
}