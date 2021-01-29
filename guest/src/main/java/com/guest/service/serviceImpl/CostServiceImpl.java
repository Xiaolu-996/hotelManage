package com.guest.service.serviceImpl;

import com.guest.pojo.po.Cost;
import com.guest.mapper.CostMapper;
import com.guest.service.CostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements CostService {
    @Autowired
    private CostMapper costMapper;

    @Override
    public List<Cost> getCostByRoomId(String roomId) {
        return costMapper.getCostByRoomId(roomId);
    }

    @Override
    public boolean settleCostByRoomId(String roomId) {
        return costMapper.settleCostByRoomId(roomId);
    }

    @Override
    public boolean removeByRoomId(String roomId) {
        return costMapper.removeByRoomId(roomId);
    }

    @Override
    public int getNotCostNum(String roomId) {
        return costMapper.getNotCostNum(roomId);
    }

    @Override
    public boolean removeByCostTypeId(Integer id) {
        return costMapper.removeByCostTypeId(id);
    }
}
