package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.PmsSearchParam;
import com.zzhao.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/11/13 0013下午 17:32
 */
public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
