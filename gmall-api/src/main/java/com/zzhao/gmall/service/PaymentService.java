package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.PaymentInfo;

/**
 * @author Administrator
 * @date 2019/11/21 0021上午 11:30
 */
public interface PaymentService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo) throws Exception;
}
