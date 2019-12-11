package com.zzhao.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zzhao.gmall.bean.UmsMember;
import com.zzhao.gmall.bean.UmsMemberReceiveAddress;
import com.zzhao.gmall.service.UserService;
import com.zzhao.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.zzhao.gmall.user.mapper.UserInfoMapper;
import com.zzhao.gmall.user.service.UserMemberCacheWorker;
import com.zzhao.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/10/28 0028下午 17:33
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Autowired
    UserMemberCacheWorker userMemberCacheWorker;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllUser() {
        return userInfoMapper.selectAll();
    }


    @Override
    public List<UmsMemberReceiveAddress> getMemberReceiveAddressById(String memberId) {
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId", memberId);
        return umsMemberReceiveAddressMapper.selectByExample(example);
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        return userMemberCacheWorker.find(umsMember);

    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();

        jedis.setex("user:" + memberId + ":token", 60 * 60 * 2, token);

        jedis.close();
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress=new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(receiveAddressId);
        return umsMemberReceiveAddressMapper.selectByPrimaryKey(umsMemberReceiveAddress);
    }
}
