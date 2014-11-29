package com.protocol7.kong;

public enum Direction {

  UP,
  DOWN,
  LEFT,
  RIGHT;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
