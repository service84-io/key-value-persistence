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
