package com.guest.service;

import com.guest.pojo.po.Cost;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Service
public interface CostService extends IService<Cost> {

    List<Cost> getCostByRoomId(String roomId);

    boolean settleCostByRoomId(String roomId);

    boolean removeByRoomId(String roomId);

    int getNotCostNum(String roomId);

    boolean removeByCostTypeId(Integer id);
}
