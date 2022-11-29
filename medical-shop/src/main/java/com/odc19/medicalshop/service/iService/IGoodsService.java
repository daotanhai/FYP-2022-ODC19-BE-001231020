package com.odc19.medicalshop.service.iService;

import com.odc19.clients.medicalShop.GoodsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IGoodsService {

    List<GoodsDTO> listAllGoods();

    GoodsDTO getGoodsById(String goodsId);

    List<GoodsDTO> getGoodsListByIds(String goodsIdListString);

    void saveNewGoods(GoodsDTO goodsDTO, List<MultipartFile> multipartFiles) throws IOException;

    GoodsDTO updateGoods(GoodsDTO goodsDTO, List<MultipartFile> multipartFiles) throws IOException;

    void deleteGoods(String goodsId);
}
