package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.PmsBaseAttrInfo;
import com.zzhao.gmall.bean.PmsBaseAttrValue;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/10/31 0031上午 10:16
 */
public interface PmsBaseAttrService {

    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);
}
