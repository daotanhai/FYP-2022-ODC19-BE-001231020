package com.odc19.medicalshop.controller;

import com.odc19.clients.medicalShop.GoodsDTO;
import com.odc19.medicalshop.service.iService.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-shop/goods")
public class GoodsController {
    @Autowired
    IGoodsService goodsService;

    @GetMapping("/list")
    List<GoodsDTO> getAllGoods() {
        return goodsService.listAllGoods();
    }

    @GetMapping("/{goodsId}")
    GoodsDTO getGoodsById(@PathVariable String goodsId) {
        return goodsService.getGoodsById(goodsId);
    }

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    void saveNewGoods(@RequestPart GoodsDTO goodsDTO,
                      @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        goodsService.saveNewGoods(goodsDTO, multipartFiles);
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    GoodsDTO updateGoods(@RequestBody GoodsDTO goodsDTO,
                         @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        return goodsService.updateGoods(goodsDTO, multipartFiles);
    }

    @DeleteMapping("/remove/{goodsId}")
    void deleteGoods(@PathVariable String goodsId) {
        goodsService.deleteGoods(goodsId);
    }
}
