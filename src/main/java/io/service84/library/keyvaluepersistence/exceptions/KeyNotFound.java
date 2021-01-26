package io.service84.library.keyvaluepersistence.exceptions;

import java.util.function.Supplier;

public class KeyNotFound extends Exception {
  private static final long serialVersionUID = 1L;

  public static Supplier<KeyNotFound> supplier() {
    return new Supplier<>() {
      @Override
      public KeyNotFound get() {
        return new KeyNotFound();
      }
    };
  }
}
