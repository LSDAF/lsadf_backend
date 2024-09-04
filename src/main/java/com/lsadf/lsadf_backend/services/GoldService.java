package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;

public interface GoldService {

    /**
     * Check if the cache is enabled
     * @return true if cache is enabled, false otherwise
     */
    boolean isCacheEnabled();

    /**
     * Get the gold of a game save
     * @param gameSaveId the id of the game save
     * @return the gold amount
     */
    long getGold(String gameSaveId) throws NotFoundException;

    /**
     * Save the gold of a game save
     *
     * @param gameSaveId the id of the game save
     * @param gold       the gold amount
     */
    void saveGold(String gameSaveId, long gold, boolean toCache) throws NotFoundException;

    /**
     * Get the gems of a user
     * @param userId the id of the user
     * @return the gems amount
     */
    long getGems(String userId);

    /**
     * Save the gems of a user
     * @param userId the id of the user
     * @param gems the gems amount
     */
    void saveGems(String userId, long gems);

    /**
     * Get the gold entity by the id of its game save
     * @param gameSaveId the id of the game save
     * @return the gold entity
     * @throws NotFoundException
     */
    GoldEntity getGoldEntity(String gameSaveId) throws NotFoundException;
}
