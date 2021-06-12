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

package io.service84.library.keyvaluepersistence.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.service84.library.keyvaluepersistence.errors.KeyValueError;
import io.service84.library.keyvaluepersistence.exceptions.KeyNotFound;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class KeyValueServiceTests {
  public static class AClass {
    public String memberA;
    public String memberB;
  }

  @TestConfiguration
  public static class Configuration {
    @Bean
    public KeyValueService getKeyValueService() {
      return new KeyValueService();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
      return new ObjectMapper();
    }
  }

  // Test Subject
  @Autowired private KeyValueService keyValueService;

  @Test
  public void setAndGetAClass() throws KeyNotFound {
    String key = UUID.randomUUID().toString();
    AClass value = new AClass();
    value.memberA = UUID.randomUUID().toString();
    value.memberB = UUID.randomUUID().toString();
    keyValueService.setValue(key, value);
    AClass gotValue = keyValueService.getValue(key, AClass.class);
    assertEquals(value.memberA, gotValue.memberA);
    assertEquals(value.memberB, gotValue.memberB);
  }

  @Test
  public void setAndGetBadSerialization() {
    String key = UUID.randomUUID().toString();
    String value = UUID.randomUUID().toString();
    keyValueService.setValue(key, value);
    assertThrows(
        KeyValueError.class,
        () -> {
          keyValueService.getValue(key, AClass.class);
        });
  }

  @Test
  public void setAndGetString() throws KeyNotFound {
    String key = UUID.randomUUID().toString();
    String value = UUID.randomUUID().toString();
    keyValueService.setValue(key, value);
    String gotValue = keyValueService.getValue(key);
    assertEquals(value, gotValue);
  }

  @Test
  public void throwKeyNotFound() {
    String unusedKey = UUID.randomUUID().toString();
    assertThrows(
        KeyNotFound.class,
        () -> {
          keyValueService.getValue(unusedKey);
        });
  }
}
