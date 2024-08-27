package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Implementation of GameSaveService
 */
@Slf4j
@RequiredArgsConstructor
public class GameSaveServiceImpl implements GameSaveService {
    private final UserService userService;
    private final GameSaveRepository gameSaveRepository;
    private final Mapper mapper;


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity createGameSave(String userEmail) throws NotFoundException {
        log.info("Creating new save for user {}", userEmail);

        UserEntity userEntity = userService.getUserByEmail(userEmail);

        GoldEntity goldEntity = GoldEntity.builder()
                .userEmail(userEntity.getEmail())
                .build();

        GameSaveEntity gameSaveEntity = GameSaveEntity
                .builder()
                .user(userEntity)
                .goldEntity(goldEntity)
                .build();

        return gameSaveRepository.save(gameSaveEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameSaveEntity getGameSave(String saveId) throws NotFoundException {
        return gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest) throws NotFoundException {

        UserEntity userEntity = userService.getUserByEmail(creationRequest.getUserEmail());

        GoldEntity goldEntity = GoldEntity.builder()
                .userEmail(userEntity.getEmail())
                .goldAmount(creationRequest.getGold())
                .build();

        GameSaveEntity entity = GameSaveEntity.builder()
                .goldEntity(goldEntity)
                .healthPoints(creationRequest.getHealthPoints())
                .attack(creationRequest.getAttack())
                .user(userEntity)
                .build();

        if (creationRequest.getId() != null) {
            if (gameSaveRepository.existsById(creationRequest.getId())) {
                throw new AlreadyExistingGameSaveException("Game save with id " + creationRequest.getId() + " already exists");
            }
            entity.setId(creationRequest.getId());
        }

        return gameSaveRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity updateGameSave(String saveId, GameSaveUpdateRequest updateRequest) throws NotFoundException {
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
        return updateGameSaveEntity(currentGameSave, updateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity updateGameSave(String saveId, AdminGameSaveUpdateRequest updateRequest) throws NotFoundException {
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
        return updateGameSaveEntity(currentGameSave, updateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteGameSave(String saveId) throws NotFoundException {
        if (saveId == null) {
            throw new NotFoundException("Game save id is null");
        }
        if (!gameSaveRepository.existsById(saveId)) {
            log.error("Game save with id {} not found", saveId);
            throw new NotFoundException("Game save with id " + saveId + " not found");
        }
        gameSaveRepository.deleteById(saveId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<GameSaveEntity> getGameSaves() {
        return gameSaveRepository.findAllGameSaves();
    }

    private GameSaveEntity updateGameSaveEntity(GameSaveEntity gameSaveEntity, GameSaveUpdateRequest updateRequest) {
        boolean hasUpdates = false;

        if (gameSaveEntity.getAttack() != updateRequest.getAttack()) {
            gameSaveEntity.setAttack(updateRequest.getAttack());
            hasUpdates = true;
        }
        if (gameSaveEntity.getHealthPoints() != updateRequest.getHealthPoints()) {
            gameSaveEntity.setHealthPoints(updateRequest.getHealthPoints());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return gameSaveRepository.save(gameSaveEntity);
        }

        return gameSaveEntity;
    }

    private GameSaveEntity updateGameSaveEntity(GameSaveEntity gameSaveEntity, AdminGameSaveUpdateRequest adminUpdateRequest) {
        boolean hasUpdates = false;

        if (gameSaveEntity.getAttack() != adminUpdateRequest.getAttack()) {
            gameSaveEntity.setAttack(adminUpdateRequest.getAttack());
            hasUpdates = true;
        }
        if (gameSaveEntity.getHealthPoints() != adminUpdateRequest.getHealthPoints()) {
            gameSaveEntity.setHealthPoints(adminUpdateRequest.getHealthPoints());
            hasUpdates = true;
        }

        GoldEntity goldEntity = gameSaveEntity.getGoldEntity();

        if (goldEntity.getGoldAmount() != adminUpdateRequest.getGold()) {
            goldEntity.setGoldAmount(adminUpdateRequest.getGold());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return gameSaveRepository.save(gameSaveEntity);
        }

        return gameSaveEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void checkGameSaveOwnership(String saveId, String userEmail) throws ForbiddenException, NotFoundException {
        GameSaveEntity gameSaveEntity = getGameSave(saveId);

        if (!Objects.equals(gameSaveEntity.getUser().getEmail(), userEmail)) {
            throw new ForbiddenException("The given user email is not the owner of the game save");
        }
    }
}
