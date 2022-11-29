package com.odc19.booking.repository.readRepository;

import com.odc19.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingReadRepository extends JpaRepository<BookingEntity, String> {
    BookingEntity findBookingEntityByCustomerIdAndMedicalShopIdAndStartDate(String customerId, String medicalShopId, Date startDate);

    List<BookingEntity> findBookingEntitiesByCustomerId(String customerId);

    List<BookingEntity> findBookingEntitiesByMedicalShopId(String medicalShopId);
}
