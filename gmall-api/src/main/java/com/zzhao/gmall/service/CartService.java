package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.OmsCartItem;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/11/15 0015下午 14:10
 */
public interface CartService {
    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    List<OmsCartItem> cartList(String memberId);

    void checkCart(OmsCartItem omsCartItem);
}
