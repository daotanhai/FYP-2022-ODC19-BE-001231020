package com.odc19.user.Repository.writeRepository;

import com.odc19.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWriteRepository extends JpaRepository<UserEntity, String> {
}
