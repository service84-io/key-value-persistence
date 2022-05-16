/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.service84.library.keyvaluepersistence.errors;

import java.util.function.Supplier;

@SuppressWarnings("serial")
public class KeyValueError extends Error {
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

  public KeyValueError(Throwable cause) {
    super(cause);
  }
}
