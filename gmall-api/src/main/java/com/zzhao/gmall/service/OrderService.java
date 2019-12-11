package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.OmsOrder;

/**
 * @author Administrator
 * @date 2019/11/20 0020上午 11:10
 */
public interface OrderService {
    String genTradeCode(String memberId);

    String checkTradeCode(String memberId, String tradeCode);

    void saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);

    void updateOrder(OmsOrder omsOrder) throws Exception;
}
