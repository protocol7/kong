package com.protocol7.kong;

public class Pos {

  private final int x;
  private final int y;

  public Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    return prime * (prime + x) + y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;

    Pos other = (Pos) obj;
    return x == other.x && y == other.y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
