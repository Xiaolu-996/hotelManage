package com.guest.mapper;

import com.guest.pojo.po.CostType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张雪萍
 * @since 2020-12-02
 */
@Mapper
@Repository
public interface CostTypeMapper extends BaseMapper<CostType> {
    List<CostType> getCostTypeByName(String name);

    List<CostType> getAllCostType();

    boolean removeByName(String name);
}
