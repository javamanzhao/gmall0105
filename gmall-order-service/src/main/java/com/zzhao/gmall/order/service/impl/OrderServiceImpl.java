package com.zzhao.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zzhao.gmall.bean.OmsOrder;
import com.zzhao.gmall.bean.OmsOrderItem;
import com.zzhao.gmall.mq.ActiveMQUtil;
import com.zzhao.gmall.order.mapper.OmsOrderItemMapper;
import com.zzhao.gmall.order.mapper.OmsOrderMapper;
import com.zzhao.gmall.service.OrderService;
import com.zzhao.gmall.util.RedisUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2019/11/20 0020上午 11:11
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public String genTradeCode(String memberId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String uuid = UUID.randomUUID().toString();
            jedis.setex("user:" + memberId + ":" + "tradeCode", 60 * 5, uuid);
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return null;
    }

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String script =
                    "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                            "   return redis.call('del',KEYS[1]) " +
                            "else" +
                            "   return 0 " +
                            "end";
            Object result = jedis.eval(script, Collections.singletonList("user:" + memberId + ":tradeCode"),
                    Collections.singletonList(tradeCode));
            if (result != null && "1".equals(result.toString()))
                return "success";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return "fail";
    }

    @Override
    public void saveOrder(OmsOrder omsOrder) {
        // 保存订单表
        omsOrderMapper.insertSelective(omsOrder);
        String orderId = omsOrder.getId();
        // 保存订单详情
        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(omsOrderItem);
            // 删除购物车数据
            // cartService.delCart();
        }
    }

    @Override
    public OmsOrder getOrderByOutTradeNo(String outTradeNo) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(outTradeNo);
        OmsOrder omsOrder1 = omsOrderMapper.selectOne(omsOrder);

        return omsOrder1;
    }

    @Override
    public void updateOrder(OmsOrder omsOrder) throws Exception{
        Example e = new Example(OmsOrder.class);
        e.createCriteria().andEqualTo("orderSn", omsOrder.getOrderSn());
        OmsOrder omsOrderUpdate = new OmsOrder();
        omsOrderUpdate.setStatus("1");
        // 发送一个订单已支付的队列，提供给库存消费
        Connection connection = null;
        Session session = null;
        try{
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true,Session.SESSION_TRANSACTED);
            Queue payhment_success_queue = session.createQueue("ORDER_PAY_QUEUE");
            MessageProducer producer = session.createProducer(payhment_success_queue);
            //TextMessage textMessage=new ActiveMQTextMessage();//字符串文本
            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构

            omsOrderMapper.updateByExampleSelective(omsOrderUpdate,e);
            producer.send(mapMessage);
            session.commit();
        }catch (Exception ex) {
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
