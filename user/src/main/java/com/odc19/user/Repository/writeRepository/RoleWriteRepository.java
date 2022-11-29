package com.odc19.user.Repository.writeRepository;

import com.odc19.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleWriteRepository extends JpaRepository<RoleEntity, String> {
}
