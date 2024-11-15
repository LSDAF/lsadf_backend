package com.lsadf.lsadf_backend.repositories;

import com.lsadf.core.entities.CharacteristicsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicsRepository extends CrudRepository<CharacteristicsEntity, String> {
}
