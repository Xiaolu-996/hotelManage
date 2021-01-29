package com.guest.service.serviceImpl;

import com.guest.mapper.CostMapper;
import com.guest.pojo.po.CostType;
import com.guest.mapper.CostTypeMapper;
import com.guest.service.CostTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-12-02
 */
@Service
public class CostTypeServiceImpl extends ServiceImpl<CostTypeMapper, CostType> implements CostTypeService {
    @Autowired
    private CostTypeMapper costTypeMapper;
    @Override
    public List<CostType> getCostTypeByName(String name) {
        return costTypeMapper.getCostTypeByName(name);
    }

    @Override
    public List<CostType> getAllCostType() {
        return costTypeMapper.getAllCostType();
    }

    @Override
    public boolean removeByName(String name) {
        return costTypeMapper.removeByName(name);
    }
}
