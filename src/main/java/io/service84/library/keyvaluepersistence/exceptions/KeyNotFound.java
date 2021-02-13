package io.service84.library.keyvaluepersistence.exceptions;

import java.util.function.Supplier;

@SuppressWarnings("serial")
public class KeyNotFound extends Exception {
  public static Supplier<KeyNotFound> supplier() {
    return new Supplier<>() {
      @Override
      public KeyNotFound get() {
        return new KeyNotFound();
      }
    };
  }

  public KeyNotFound() {}

  public KeyNotFound(String message) {
    super(message);
  }

  public KeyNotFound(String message, Throwable cause) {
    super(message, cause);
  }
}
