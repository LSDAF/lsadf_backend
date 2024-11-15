package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.core.entities.CharacteristicsEntity;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.core.models.Characteristics;
import com.lsadf.lsadf_backend.repositories.CharacteristicsRepository;
import com.lsadf.lsadf_backend.services.CharacteristicsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CharacteristicsServiceImpl implements CharacteristicsService {

    private final CharacteristicsRepository characteristicsRepository;
    private final Cache<Characteristics> characteristicsCache;
    private final Mapper mapper;

    public CharacteristicsServiceImpl(CharacteristicsRepository characteristicsRepository,
                               Cache<Characteristics> characteristicsCache,
                               Mapper mapper) {
        this.characteristicsRepository = characteristicsRepository;
        this.characteristicsCache = characteristicsCache;

        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Characteristics getCharacteristics(String gameSaveId) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }
        if (characteristicsCache.isEnabled()) {
            Optional<Characteristics> optionalCachedCharacteristics = characteristicsCache.get(gameSaveId);
            if (optionalCachedCharacteristics.isPresent()) {
                Characteristics characteristics = optionalCachedCharacteristics.get();
                if (characteristics.getAttack() == null || characteristics.getCritChance() == null || characteristics.getCritDamage() == null || characteristics.getHealth() == null || characteristics.getResistance() == null) {
                    CharacteristicsEntity characteristicsEntity = getCharacteristicsEntity(gameSaveId);
                    return mergeCharacteristics(characteristics, characteristicsEntity);
                }
                return characteristics;
            }
        }
        CharacteristicsEntity characteristicsEntity = getCharacteristicsEntity(gameSaveId);

        return mapper.mapCharacteristicsEntityToCharacteristics(characteristicsEntity);
    }

    /**
     * Merge the characteristics POJO with the characteristics entity from the database
     * @param characteristics the characteristics POJO
     * @param characteristicsEntity the characteristics entity from the database
     * @return the merged characteristics POJO
     */
    private static Characteristics mergeCharacteristics(Characteristics characteristics, CharacteristicsEntity characteristicsEntity) {
        if (characteristics.getAttack() == null) {
            characteristics.setAttack(characteristicsEntity.getAttack());
        }

        if (characteristics.getCritChance() == null) {
            characteristics.setCritChance(characteristicsEntity.getCritChance());
        }

        if (characteristics.getCritDamage() == null) {
            characteristics.setCritDamage(characteristicsEntity.getCritDamage());
        }

        if (characteristics.getHealth() == null) {
            characteristics.setHealth(characteristicsEntity.getHealth());
        }

        if (characteristics.getResistance() == null) {
            characteristics.setResistance(characteristicsEntity.getResistance());
        }

        return characteristics;
    }

    @Override
    @Transactional
    public void saveCharacteristics(String gameSaveId, Characteristics characteristics, boolean toCache) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }
        if (characteristics == null || isCharacteristicsNull(characteristics)) {
            throw new IllegalArgumentException("Characteristics cannot be null");
        }
        if (toCache) {
            characteristicsCache.set(gameSaveId, characteristics);
        } else {
            saveCharacteristicsToDatabase(gameSaveId, characteristics);
        }
    }

    /**
     * Get the characteristics entity from the database
     * @param gameSaveId the id of the game save
     * @return the characteristics entity
     * @throws NotFoundException if the characteristics entity is not found
     */
    private CharacteristicsEntity getCharacteristicsEntity(String gameSaveId) throws NotFoundException {
        return characteristicsRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Characteristics entity not found for game save id " + gameSaveId));
    }

    /**
     * Save the gold amount to the database
     * @param gameSaveId the id of the game save
     * @param characteristics the characteristics POJO
     * @throws NotFoundException if the characteristics entity is not found
     */
    private void saveCharacteristicsToDatabase(String gameSaveId, Characteristics characteristics) throws NotFoundException {
        CharacteristicsEntity characteristicsEntity = getCharacteristicsEntity(gameSaveId);

        boolean hasUpdates = false;

        if (characteristics.getAttack() != null) {
            characteristicsEntity.setAttack(characteristics.getAttack());
            hasUpdates = true;
        }
        if (characteristics.getCritChance() != null) {
            characteristicsEntity.setCritChance(characteristics.getCritChance());
            hasUpdates = true;
        }
        if (characteristics.getCritDamage() != null) {
            characteristicsEntity.setCritDamage(characteristics.getCritDamage());
            hasUpdates = true;
        }
        if (characteristics.getHealth() != null) {
            characteristicsEntity.setHealth(characteristics.getHealth());
            hasUpdates = true;
        }
        if (characteristics.getResistance() != null) {
            characteristicsEntity.setResistance(characteristics.getResistance());
            hasUpdates = true;
        }

        if (hasUpdates) {
            characteristicsRepository.save(characteristicsEntity);
        }
    }

    /**
     * Check if the characteristics is all null
     * @param characteristics the characteristics POJO
     * @return true if all fields are null, false otherwise
     */
    private static boolean isCharacteristicsNull(Characteristics characteristics) {
        return characteristics.getAttack() == null
                && characteristics.getCritChance() == null
                && characteristics.getCritDamage() == null
                && characteristics.getHealth() == null
                && characteristics.getResistance() == null;
    }
}
