package com.zzhao.gmall.manage.mapper;

import com.zzhao.gmall.bean.PmsProductSaleAttr;
import com.zzhao.gmall.bean.PmsProductSaleAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/11/5 0005下午 15:40
 */
public interface PmsProductSaleAttrValueMapper extends Mapper<PmsProductSaleAttrValue> {

    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") String skuId);
}
