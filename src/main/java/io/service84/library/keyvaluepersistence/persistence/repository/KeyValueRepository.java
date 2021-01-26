package io.service84.library.keyvaluepersistence.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.service84.library.keyvaluepersistence.persistence.model.KeyValue;

@Repository("036D1001-3BDC-476A-800E-042886341F00")
public interface KeyValueRepository
    extends JpaRepository<KeyValue, UUID>, JpaSpecificationExecutor<KeyValue> {
  Optional<KeyValue> getByKey(String key);
}
