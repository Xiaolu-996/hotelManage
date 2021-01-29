package com.guest.service;

import com.guest.pojo.po.Room;
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
public interface RoomService extends IService<Room> {

    List<Room> getRoomsByType(String rank);
}
