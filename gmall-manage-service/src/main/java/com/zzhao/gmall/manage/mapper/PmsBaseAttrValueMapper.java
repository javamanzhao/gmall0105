package com.zzhao.gmall.manage.mapper;

import com.zzhao.gmall.bean.PmsBaseAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/10/31 0031上午 10:33
 */
public interface PmsBaseAttrValueMapper extends Mapper<PmsBaseAttrValue> {

    int batchSave(@Param("pmsBaseAttrValues") List<PmsBaseAttrValue> pmsBaseAttrValues,@Param("attrId") String attrId);

    int deleteByAttrId(@Param("attrId") String attrId);
}
