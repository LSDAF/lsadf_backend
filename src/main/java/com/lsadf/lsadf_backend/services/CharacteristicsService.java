package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Characteristics;

public interface CharacteristicsService {
    /**
     * Get the characteristics of a game save
     * @param gameSaveId the game save id
     * @return the characteristics
     * @throws NotFoundException if the characteristics are not found
     */
    Characteristics getCharacteristics(String gameSaveId) throws NotFoundException;

    /**
     * Save the characteristics of a game save
     * @param gameSaveId the game save id
     * @param characteristics the characteristics to save
     * @param toCache true if the characteristics should be saved to cache, false otherwise
     * @throws NotFoundException if the characteristics are not found
     */
    void saveCharacteristics(String gameSaveId, Characteristics characteristics, boolean toCache) throws NotFoundException;
}
