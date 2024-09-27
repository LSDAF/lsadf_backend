package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.*;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;

import java.util.List;
import java.util.stream.Stream;

/**
 * Service for managing game saves
 */
public interface GameSaveService {
    /**
     * Creates a new game save for the user
     *
     * @param userEmail the user email
     * @return the created game save
     * @throws NotFoundException the not found exception
     */
    GameSaveEntity createGameSave(String userEmail) throws NotFoundException;

    /**
     * Creates a new game save
     *
     * @param creationRequest the admin creation request
     * @return the created game save
     * @throws NotFoundException the not found exception
     */
    GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest) throws NotFoundException, AlreadyExistingGameSaveException;

    /**
     * Gets a game save
     *
     * @param saveId the save id
     * @return the game save
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    GameSaveEntity getGameSave(String saveId) throws NotFoundException;

    /**
     * Updates a game save
     *
     * @param saveId        the save id
     * @param updateRequest the update request
     * @return the updated game save
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    GameSaveEntity updateGameSave(String saveId, GameSaveUpdateRequest updateRequest) throws ForbiddenException, NotFoundException, UnauthorizedException, AlreadyTakenNicknameException;

    /**
     * Updates a game save from admin side
     *
     * @param saveId        the save id
     * @param updateRequest the admin update request
     * @return the updated game save
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    GameSaveEntity updateGameSave(String saveId, AdminGameSaveUpdateRequest updateRequest) throws ForbiddenException, NotFoundException, UnauthorizedException, AlreadyTakenNicknameException;


    /**
     * Deletes a game save
     *
     * @param saveId the save id
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    void deleteGameSave(String saveId) throws NotFoundException;

    /**
     * Gets all game saves
     *
     * @return the stream of game saves
     */
    Stream<GameSaveEntity> getGameSaves();


    /**
     * Gets all game saves of a user
     *
     * @param userEmail the user email
     * @return the stream of game saves
     */
    List<GameSaveEntity> getGameSavesByUserEmail(String userEmail);

    /**
     * Checks if the user owns the game save
     *
     * @param saveId    the game save id
     * @param userEmail the user email
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    void checkGameSaveOwnership(String saveId, String userEmail) throws ForbiddenException, NotFoundException;
}
