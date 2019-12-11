package com.zzhao.gmall.service;



import com.zzhao.gmall.bean.UmsMember;
import com.zzhao.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/10/28 0028下午 17:32
 */
public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getMemberReceiveAddressById(String memberId);

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, String id);

    UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId);
}
