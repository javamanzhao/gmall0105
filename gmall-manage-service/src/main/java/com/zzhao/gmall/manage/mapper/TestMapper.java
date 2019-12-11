package com.zzhao.gmall.manage.mapper;

import com.zzhao.gmall.bean.TestBean;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/12/3 0003上午 9:46
 */
public interface TestMapper extends Mapper<TestBean> {

    void  updateNum(@Param("id") String id);

    List<TestBean> selectBean(@Param("id") String id);

    void insertBean(@Param("id") String id);
}
