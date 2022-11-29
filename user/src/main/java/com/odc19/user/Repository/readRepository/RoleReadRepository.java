package com.odc19.user.Repository.readRepository;

import com.odc19.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleReadRepository extends JpaRepository<RoleEntity, String> {
    Set<RoleEntity> findAllByRoleNameIn(List<String> roleNames);
}
