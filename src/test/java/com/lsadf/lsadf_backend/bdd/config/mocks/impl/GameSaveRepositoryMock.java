package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Mock implementation of the GameSaveRepository
 */
public class GameSaveRepositoryMock extends ARepositoryMock<GameSaveEntity> {

    private final CurrencyRepository currencyRepository;

    public GameSaveRepositoryMock(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
        currencyRepository.deleteById(id);
    }

    @Override
    public GameSaveEntity save(GameSaveEntity entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        GameSaveEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entities.put(entity.getId(), entity);
            CurrencyEntity currencyEntity = entity.getCurrencyEntity();
            if (currencyEntity != null) {
                currencyRepository.save(currencyEntity);
            }
            return entity;
        }
        toUpdate.setAttack(entity.getAttack());
        toUpdate.setHealthPoints(entity.getHealthPoints());
        toUpdate.setUpdatedAt(now);

        CurrencyEntity currencyEntity = entity.getCurrencyEntity();
        CurrencyEntity updatedCurrencyEntity = currencyRepository.save(currencyEntity);

        toUpdate.setCurrencyEntity(updatedCurrencyEntity);
        entities.put(entity.getId(), toUpdate);

        return toUpdate;
    }

    public Stream<GameSaveEntity> findAllSaveGames() {
        return entities.values().stream();
    }

    public List<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userId) {
        return entities.values().stream().filter(gameSaveEntity -> gameSaveEntity.getUser().getEmail().equals(userId)).toList();
    }
}
