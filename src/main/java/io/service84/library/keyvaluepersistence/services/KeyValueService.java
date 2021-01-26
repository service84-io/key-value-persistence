package io.service84.library.keyvaluepersistence.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.service84.library.keyvaluepersistence.errors.KeyValueError;
import io.service84.library.keyvaluepersistence.exceptions.KeyNotFound;
import io.service84.library.keyvaluepersistence.persistence.model.KeyValue;
import io.service84.library.keyvaluepersistence.persistence.repository.KeyValueRepository;

@Service("5470A35A-0F8E-4193-B15D-F7036A85E7C1")
public class KeyValueService {
  @Autowired private ObjectMapper objectMapper;
  @Autowired private KeyValueRepository repository;

  public String getValue(String key) throws KeyNotFound {
    KeyValue keyValue = repository.getByKey(key).orElseThrow(KeyNotFound.supplier());
    return keyValue.getValue();
  }

  public <T> T getValue(String key, Class<T> clazz) throws KeyNotFound {
    try {
      KeyValue keyValue = repository.getByKey(key).orElseThrow(KeyNotFound.supplier());
      return objectMapper.readValue(keyValue.getValue(), clazz);
    } catch (JsonMappingException e) {
      throw new KeyValueError();
    } catch (JsonProcessingException e) {
      throw new KeyValueError();
    }
  }

  public void setValue(String key, Object value) {
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
    try {
      String serializedValue = objectMapper.writeValueAsString(value);
      KeyValue keyValue = repository.getByKey(key).orElse(new KeyValue(key));
      keyValue.setValue(serializedValue);
      repository.saveAndFlush(keyValue);
    } catch (JsonProcessingException e) {
      throw new KeyValueError();
    }
  }
}
