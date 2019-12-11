package com.zzhao.gmall.manage.mapper;



import com.zzhao.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @param
 * @return
 */
public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {


    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@Param("productId") String productId);
}
