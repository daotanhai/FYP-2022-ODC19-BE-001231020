package com.odc19.rating.repository.writeRepository;

import com.odc19.rating.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteRatingRepository extends JpaRepository<RatingEntity, String> {
}
