package com.zzhao.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zzhao.gmall.bean.PmsBaseCatalog1;
import com.zzhao.gmall.bean.PmsBaseCatalog2;
import com.zzhao.gmall.bean.PmsBaseCatalog3;
import com.zzhao.gmall.manage.mapper.Catalog1Mapper;
import com.zzhao.gmall.manage.mapper.Catalog2Mapper;
import com.zzhao.gmall.manage.mapper.Catalog3Mapper;
import com.zzhao.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/10/30 0030下午 16:44
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    Catalog1Mapper catalog1Mapper;

    @Autowired
    Catalog2Mapper catalog2Mapper;

    @Autowired
    Catalog3Mapper catalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return catalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String  catalog1Id) {
        Example example=new Example(PmsBaseCatalog2.class);
        example.createCriteria().andEqualTo("catalog1Id",catalog1Id);
        return catalog2Mapper.selectByExample(example);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        Example example=new Example(PmsBaseCatalog3.class);
        example.createCriteria().andEqualTo("catalog2Id",catalog2Id);
        return catalog3Mapper.selectByExample(example);
    }


}
