package com.protocol7.kong;


public class Tile {

  private final TileType type;
  private final Pos pos;

  public Tile(final TileType type, final Pos pos) {
    this.type = type;
    this.pos = pos;
  }

  public TileType getType() {
    return type;
  }

  public Pos getPos() {
    return pos;
  }

  @Override
  public String toString() {
    return pos + "," + type;
  }

  @Override
  public int hashCode() {
    return pos.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Tile other = (Tile) obj;
    return other.pos.equals(pos);
  }


}