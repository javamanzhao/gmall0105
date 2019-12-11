package com.zzhao.gmall.manage.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.zzhao.gmall.bean.PmsBaseAttrInfo;
import com.zzhao.gmall.bean.PmsBaseAttrValue;
import com.zzhao.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.zzhao.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.zzhao.gmall.service.PmsBaseAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author Administrator
 * @date 2019/10/31 0031上午 10:18
 */
@Service
public class PmsBaseAttrValueServiceImpl implements PmsBaseAttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {
        Example example = new Example(PmsBaseAttrInfo.class);
        example.createCriteria().andEqualTo("catalog3Id", catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectByExample(example);
        pmsBaseAttrInfos.forEach(pmsBaseAttrInfo -> {
            String attrId = pmsBaseAttrInfo.getId();
            Example example2 = new Example(PmsBaseAttrValue.class);
            example2.createCriteria().andEqualTo("attrId", attrId);
            pmsBaseAttrInfo.setAttrValueList(pmsBaseAttrValueMapper.selectByExample(example2));

        });
        return pmsBaseAttrInfos;
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId", attrId);
        return pmsBaseAttrValueMapper.selectByExample(example);

    }

    @Override
    @Transactional(propagation = REQUIRED)
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        //无id表示新增操作
        if (pmsBaseAttrInfo.getId() == null) {
            pmsBaseAttrInfoMapper.insert(pmsBaseAttrInfo);
            if (CollectionUtils.isNotEmpty(pmsBaseAttrInfo.getAttrValueList())) {
                pmsBaseAttrValueMapper.batchSave(pmsBaseAttrInfo.getAttrValueList(), pmsBaseAttrInfo.getId());
            }
        } else {
            pmsBaseAttrInfoMapper.updateByPrimaryKey(pmsBaseAttrInfo);
            pmsBaseAttrValueMapper.deleteByAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.batchSave(pmsBaseAttrInfo.getAttrValueList(), pmsBaseAttrInfo.getId());
        }
        return "success";
    }


}
