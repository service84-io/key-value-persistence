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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.service84.library.keyvaluepersistence.errors.KeyValueError;
import io.service84.library.keyvaluepersistence.exceptions.KeyNotFound;
import io.service84.library.keyvaluepersistence.persistence.model.KeyValue;
import io.service84.library.keyvaluepersistence.persistence.repository.KeyValueRepository;

@Service("5470A35A-0F8E-4193-B15D-F7036A85E7C1")
public class KeyValueService {
  private static final Logger logger = LoggerFactory.getLogger(KeyValueService.class);

  @Autowired private ObjectMapper objectMapper;
  @Autowired private KeyValueRepository repository;

  public String getValue(String key) throws KeyNotFound {
    logger.debug("getValue");
    KeyValue keyValue = repository.getByKey(key).orElseThrow(KeyNotFound.supplier());
    return keyValue.getValue();
  }

  public <T> T getValue(String key, Class<T> clazz) throws KeyNotFound, KeyValueError {
    logger.debug("getValue");
    KeyValue keyValue = repository.getByKey(key).orElseThrow(KeyNotFound.supplier());

    try {
      return objectMapper.readValue(keyValue.getValue(), clazz);
    } catch (Exception e) {
      // We use a catch all to accommodate all possible exceptions
      // thrown by all library versions, that is Jackson did not
      // sufficiently comply with backwards compatibility, but it
      // could also be that we have an unreasonable definition of
      // backwards compatibility.
      // Jackson may remove Exceptions from the throws clause, but
      // Jackson should have continue to include class definitions
      // for obsolete Exceptions
      throw new KeyValueError();
    }
  }

  public void setValue(String key, Object value) {
    logger.debug("setValue");
    try {
      setValueHelper(key, value);
    } catch (Exception e) {
      // This is a catch all, Transaction issues, Unique Violation, and others
      // We should catch specific Exceptions.
      // Final Attempt
      setValueHelper(key, value);
    }
  }

  private void setValueHelper(String key, Object value) {
    String valueAsString = stringifyValue(value);
    KeyValue keyValue = repository.getByKey(key).orElse(new KeyValue(key));
    keyValue.setValue(valueAsString);
    repository.saveAndFlush(keyValue);
  }

  private String stringifyValue(Object value) {
    if (value.getClass().equals(String.class)) {
      return (String) value;
    }

    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception e) {
      // We use a catch all to accommodate all possible exceptions
      // thrown by all library versions, that is Jackson did not
      // sufficiently comply with backwards compatibility, but it
      // could also be that we have an unreasonable definition of
      // backwards compatibility.
      // Jackson may remove Exceptions from the throws clause, but
      // Jackson should have continue to include class definitions
      // for obsolete Exceptions
      throw new KeyValueError();
    }
  }
}
