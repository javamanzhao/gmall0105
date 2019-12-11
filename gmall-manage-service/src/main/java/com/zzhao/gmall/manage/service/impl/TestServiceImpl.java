package com.zzhao.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zzhao.gmall.bean.TestBean;
import com.zzhao.gmall.manage.mapper.TestMapper;
import com.zzhao.gmall.service.TestService;
import com.zzhao.gmall.util.RedisUtil;
import io.shardingjdbc.core.api.HintManager;
import io.shardingjdbc.core.hint.HintManagerHolder;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/12/3 0003上午 9:49
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestMapper testMapper;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public void updateTest(String id) {
        RReadWriteLock rLock = redissonClient.getReadWriteLock(id);
        rLock.readLock().lock();
        redisUtil.getJedis().incr("num:1");
        rLock.readLock().unlock();

       /*  Example example = new Example(TestBean.class);
            example.createCriteria().andEqualTo("id", id);
            TestBean testBean1 = testMapper.selectOneByExample(example);
            if (testBean1 != null) {
                BigDecimal number= new BigDecimal("1.00");
                testBean1.setNum(testBean1.getNum().add(number));
                testMapper.updateByPrimaryKey(testBean1);
            }*/
    }

    @Override
    public void updateTest2(String id) {
        RReadWriteLock rLock = redissonClient.getReadWriteLock(id);
        rLock.writeLock().lock();
        redisUtil.getJedis().incr("num:2");
        rLock.writeLock().unlock();

       /*  Example example = new Example(TestBean.class);
            example.createCriteria().andEqualTo("id", id);
            TestBean testBean1 = testMapper.selectOneByExample(example);
            if (testBean1 != null) {
                BigDecimal number= new BigDecimal("1.00");
                testBean1.setNum(testBean1.getNum().add(number));
                testMapper.updateByPrimaryKey(testBean1);
            }*/
    }

    @Override
    public void insertBean() {
        testMapper.insertBean("2");
        System.out.println("插入成功");
    }
    @Override
    @Transactional
    public void select(String id) {
        HintManager.getInstance().setMasterRouteOnly();
        List<TestBean> testBean=testMapper.selectBean(id);
        System.out.println(testBean);
        try {
            Thread.sleep(7000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HintManagerHolder.clear();
        HintManager.getInstance().setMasterRouteOnly();
        List<TestBean> testBeans=testMapper.selectBean(id);
        System.out.println(testBeans);
    }
}
