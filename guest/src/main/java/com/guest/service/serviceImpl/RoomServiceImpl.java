package com.guest.service.serviceImpl;

import com.guest.pojo.po.Room;
import com.guest.mapper.RoomMapper;
import com.guest.service.RoomService;
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
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Room> getRoomsByType(String rank) {
        return roomMapper.getRoomsByType(rank);
    }
}
