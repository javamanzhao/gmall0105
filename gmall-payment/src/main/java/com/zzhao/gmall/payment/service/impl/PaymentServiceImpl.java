package com.zzhao.gmall.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zzhao.gmall.bean.PaymentInfo;
import com.zzhao.gmall.mq.ActiveMQUtil;
import com.zzhao.gmall.payment.mapper.PaymentInfoMapper;
import com.zzhao.gmall.service.PaymentService;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;


/**
 * @author Administrator
 * @date 2019/11/21 0021上午 11:32
 */
@Service(retries = -1)
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }


    @Transactional(propagation = REQUIRED)
    @Override
    public void updatePayment(PaymentInfo paymentInfo) throws Exception {
        String orderSn = paymentInfo.getOrderSn();
        Example e = new Example(PaymentInfo.class);
        e.createCriteria().andEqualTo("orderSn", orderSn);
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            paymentInfoMapper.updateByExampleSelective(paymentInfo, e);
            // 支付成功后，引起的系统服务-》订单服务的更新-》库存服务-》物流服务
            // 调用mq发送支付成功的消息
            Queue payhment_success_queue = session.createQueue("PAYHMENT_SUCCESS_QUEUE");
            MessageProducer producer = session.createProducer(payhment_success_queue);

            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构

            mapMessage.setString("out_trade_no", paymentInfo.getOrderSn());

            producer.send(mapMessage);

            session.commit();
        }  catch (Exception ex) {
            // 消息回滚
            session.rollback();
            throw ex;
        } finally {
            try {
                connection.close();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }

    }

}
