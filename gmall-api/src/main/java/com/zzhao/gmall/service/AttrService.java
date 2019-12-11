package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.PmsBaseAttrInfo;

import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 * @date 2019/11/13 0013下午 20:14
 */
public interface AttrService {
    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
