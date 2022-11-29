package com.odc19.medicalshop.repository.readRepository;

import com.odc19.medicalshop.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReadRepository extends JpaRepository<GoodsEntity, String> {
    @Query(value = "SELECT EXISTS( " +
            " SELECT * FROM goods WHERE medical_shop_id =:medicalShopId " +
            " AND goodsname =:goodsName )"
            , nativeQuery = true)
    boolean checkGoodsNameIsExistInMedicalShop(@Param("medicalShopId") String medicalShopId,
                                               @Param("goodsName") String goodsName);

    @Query(value = "SELECT * FROM goods " +
            "where medical_shop_id =:medicalShopId ", nativeQuery = true)
    List<GoodsEntity> findGoodsByMedicalShopId(@Param("medicalShopId") String medicalShopId);

    @Query(value = "SELECT * FROM goods WHERE goods_id =:goodsId", nativeQuery = true)
    GoodsEntity findGoodsById(@Param("goodsId") String goodsId);

//    List<GoodsEntity> findGoodsEntitiesByGoodsIdIn(String )
}
