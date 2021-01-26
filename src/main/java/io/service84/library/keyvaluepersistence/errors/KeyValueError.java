package io.service84.library.keyvaluepersistence.errors;

import java.util.function.Supplier;

public class KeyValueError extends Error {
  public static final long serialVersionUID = 1L;

  public static Supplier<KeyValueError> supplier() {
    return new Supplier<>() {
      @Override
      public KeyValueError get() {
        return new KeyValueError();
      }
    };
  }

  public KeyValueError() {}

  public KeyValueError(String message) {
    super(message);
  }

  public KeyValueError(String message, Throwable cause) {
    super(message, cause);
  }
}
