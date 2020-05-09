package com.bitfty.mall.dao;


import com.bitfty.mall.entity.ProductDetail;

/**
 * @author zhangmi
 */
public interface ProductDetailDao {

    int delete(String id);

    int insert(ProductDetail record);

    int update(ProductDetail record);

    ProductDetail findById(String id);

    ProductDetail findByProdId(String prodId);
}