package com.odc19.user.Repository.readRepository;

import com.odc19.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReadRepository extends JpaRepository<UserEntity, String> {
    @Query(value = "SELECT * FROM appuser WHERE user_id !=:userId " +
            " AND username =:userName", nativeQuery = true)
    UserEntity checkExistedUserNameForUpdate(@Param("userName") String userName,
                                             @Param("userId") String userId);

    @Query(value = "SELECT * FROM appuser WHERE username =:userName", nativeQuery = true)
    UserEntity checkExistedUserNameForSaveNew(@Param("userName") String userName);

    UserEntity getUserEntityByUserName(String userName);

    @Query(value = "SELECT * FROM appuser WHERE randomtokenresetpassword =:token" +
            " AND istokenused =:isTokenUsed", nativeQuery = true)
    UserEntity getUserEntityByRandomTokenResetPassword(@Param(value = "token") String token,
                                                       @Param(value = "isTokenUsed") boolean isTokenUsed);

    @Query(value = "SELECT * FROM appuser  " +
            " INNER JOIN user_role ur on appuser.user_id = ur.user_id " +
            " WHERE username =:userName ", nativeQuery = true)
    UserEntity getUserEntityAndRolesByUserName(@Param(value = "userName") String userName);
}
