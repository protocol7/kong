package com.protocol7.kong;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
  WALL('#', "wall"),
  EMPTY(' ', "empty"),
  USER('u', "user"),
  MONKEY('m', "monkey"),
  OTHER_MONKEY('o', "other"),
  SONG('t', "song"),
  ALBUM('a', "album"),
  PLAYLIST('p', "playlist"),
  BANANA('b', "banana"),
  TRAP('*', "trap"),
  OPEN_DOOR('d', "open-door"),
  CLOSED_DOOR('c', "closed-door"),
  LEVER('l', "lever"),
  TUNNEL1('1', "tunnel-1"), // YOLO
  TUNNEL2('2', "tunnel-2"),
  TUNNEL3('3', "tunnel-3"),
  TUNNEL4('4', "tunnel-4"),
  TUNNEL5('5', "tunnel-5"),
  TUNNEL6('6', "tunnel-6"),
  TUNNEL7('7', "tunnel-7"),
  TUNNEL8('8', "tunnel-8"),
  TUNNEL9('9', "tunnel-9");

  private static final Map<Character, TileType> CHARS = new HashMap<Character, TileType>();
  private static final Map<String, TileType> WORDS = new HashMap<String, TileType>();

  static {
    for (final TileType tile : TileType.values()) {
      CHARS.put(tile.c, tile);
    }
  }

  static {
    for (final TileType tile : TileType.values()) {
      WORDS.put(tile.word, tile);
    }
  }

  public static TileType fromChar(final char c) {
    final TileType tile = CHARS.get(c);

    if (tile != null) {
      return tile;
    } else {
      throw new IllegalArgumentException("Unknown tile type: " + c);
    }
  }

  public static TileType fromWord(final String word) {
    final TileType tile = WORDS.get(word);

    if (tile != null) {
      return tile;
    } else {
      throw new IllegalArgumentException("Unknown tile type: " + word);
    }
  }

  private final char c;
  final String word;

  private TileType(final char c, final String word) {
    this.c = c;
    this.word = word;
  }

  public char getChar() {
    return c;
  }

  public boolean isFree() {
    return this == EMPTY || this == SONG;
  }

  public boolean isTunnel() {
    return word.startsWith("tunnel-");
  }

  @Override
  public String toString() {
    return word;
  }
}