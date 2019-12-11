package com.zzhao.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.zzhao.gmall.bean.PmsBaseAttrInfo;
import com.zzhao.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.zzhao.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.zzhao.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {

        String valueIdStr = StringUtils.join(valueIdSet, ",");//41,45,46
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }


}
