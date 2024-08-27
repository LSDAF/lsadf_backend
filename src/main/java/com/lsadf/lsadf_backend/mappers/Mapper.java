package com.lsadf.lsadf_backend.mappers;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;

public interface Mapper {
    /**
     * Maps GameSaveEntity to GameSave
     *
     * @param gameSaveEntity GameSaveEntity
     * @return
     */
    GameSave mapToGameSave(GameSaveEntity gameSaveEntity);

    /**
     * Maps UserEntity to User
     *
     * @param userEntity UserEntity
     * @return User
     */
    User mapToUser(UserEntity userEntity);

    /**
     * Maps UserEntity to UserAdminDetails
     *
     * @param userEntity UserEntity
     * @return UserAdminDetails
     */
    UserAdminDetails mapToUserAdminDetails(UserEntity userEntity);

    /**
     * Maps LocalUser to UserInfo
     *
     * @param localUser LocalUser
     * @return UserInfo
     */
    UserInfo mapLocalUserToUserInfo(LocalUser localUser);

    /**
     * Maps UserEntity to UserInfo
     *
     * @param userEntity UserEntity
     * @return UserInfo
     */
    UserInfo mapUserEntityToUserInfo(UserEntity userEntity);

    /**
     * Maps UserEntity to LocalUser
     * @param user UserEntity
     * @return LocalUser
     */
    LocalUser mapUserEntityToLocalUser(UserEntity user);

    /**
     * Maps GoldEntity to Gold
     * @param goldEntity GoldEntity
     * @return Gold
     */
    Gold mapGoldEntityToGold(GoldEntity goldEntity);
}
