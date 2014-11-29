package com.protocol7.kong;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class Command {

  public static Command use(final String item) {
    return new Command("use", item);
  }

  public static Command move(final Direction...directions) {
    return new Command("move", directions);
  }

  public static Command move(final List<Direction> directions) {
    return new Command("move", directions);
  }

  public static Command moveOrIdle(final Optional<Direction> direction) {
    if (direction.isPresent()) {
      return move(direction.get());
    } else {
      return idle();
    }
  }

  public static Command idle() {
    return new Command("idle");
  }

  private final String command;
  private final List<Direction> directions;
  private final String item;

  public Command(final String command, final Direction... directions) {
    this.command = command;
    this.directions = Lists.newArrayList(directions);
    this.item = null;
  }

  public Command(final String command, final List<Direction> directions) {
    this.command = command;
    this.directions = directions;
    this.item = null;
  }

  public Command(final String command, final String item) {
    this.command = command;
    this.directions = Collections.emptyList();
    this.item = item;
  }

  public Map<String, Object> toMap() {
    final Map<String, Object> nextCommand = new HashMap<String, Object>();
    nextCommand.put("command", command);
    if (directions.size() > 1) {
      final List<String> dirStr = Lists.newArrayList();
      for (final Direction dir : directions) {
        dirStr.add(dir.toString());
      }
      nextCommand.put("directions", dirStr);
    } else if (directions.size() == 1) {
      nextCommand.put("direction", directions.get(0).toString());
    }
    if (item != null) {
      nextCommand.put("item", item);
    }
    return nextCommand;
  }


}
