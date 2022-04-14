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

package io.service84.library.keyvaluepersistence.envvars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.service84.library.keyvaluepersistence.persistence.models.KeyValue;
import io.service84.library.keyvaluepersistence.persistence.repositories.KeyValueRepository;
import io.service84.library.keyvaluepersistence.services.KeyValueService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaAuditing
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class KeyValueAuditTests {
  @TestConfiguration
  public static class Configuration {
    @Autowired private EntityManagerFactory entityManagerFactory;

    @Bean
    AuditReader auditReader() {
      return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }

    @Bean
    public KeyValueService getKeyValueService() {
      return new KeyValueService();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
      return new ObjectMapper();
    }
  }

  @Autowired private KeyValueService keyValueService;
  @Autowired private KeyValueRepository keyValueRepository;
  @Autowired private AuditReader auditReader;

  @Test
  public void exists() {
    assertNotNull(keyValueService);
    assertNotNull(keyValueRepository);
    assertNotNull(auditReader);
  }

  private UUID getKeyValueId(KeyValue keyValue) {
    try {
      Field field = keyValue.getClass().getDeclaredField("id");
      field.setAccessible(true);
      return (UUID) field.get(keyValue);
    } catch (Exception e) {
      return null;
    }
  }

  @Test
  public void isAuditedClass() {
    assertTrue(auditReader.isEntityClassAudited(KeyValue.class));
  }

  @Test
  public void isAuditedName() {
    assertTrue(auditReader.isEntityNameAudited(KeyValue.class.getCanonicalName()));
  }

  @Test
  public void versions() {
    String key = UUID.randomUUID().toString();

    for (int value = 0; value < 10; value++) {
      keyValueService.setValue(key, String.valueOf(value));
    }

    KeyValue keyValue = keyValueRepository.getByKey(key).get();
    UUID id = getKeyValueId(keyValue);
    List<Number> revisions = auditReader.getRevisions(KeyValue.class, id);
    assertEquals(10, revisions.size());
  }
}
