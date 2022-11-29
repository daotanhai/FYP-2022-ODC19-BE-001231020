package com.odc19.booking.repository.writeRepository;

import com.odc19.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingWriteRepository extends JpaRepository<BookingEntity, String> {
}
