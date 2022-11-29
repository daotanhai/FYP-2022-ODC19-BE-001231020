package com.odc19.location.repository.writeRepository;

import com.odc19.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationWriteRepository extends JpaRepository<LocationEntity, String> {
}
