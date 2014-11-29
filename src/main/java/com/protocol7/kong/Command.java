package com.protocol7.kong;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

public class Command {

  public static Command move(final Direction direction) {
    return new Command("move", direction.name().toLowerCase());
  }

  public static Command moveOrIdle(final Optional<Direction> direction) {
    if (direction.isPresent()) {
      return new Command("move", direction.get().name().toLowerCase());
    } else {
      return idle();
    }
  }

  public static Command idle() {
    return new Command("idle", null);
  }

  private final String command;
  private final String direction;

  public Command(final String command, final String direction) {
    this.command = command;
    this.direction = direction;
  }

  public Map<String, Object> toMap() {
    final Map<String, Object> nextCommand = new HashMap<String, Object>();
    nextCommand.put("command", command);
    if (direction != null) {
      nextCommand.put("direction", direction);
    }
    return nextCommand;
  }


}
