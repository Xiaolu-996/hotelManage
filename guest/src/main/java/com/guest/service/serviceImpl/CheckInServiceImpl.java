package com.guest.service.serviceImpl;

import com.guest.pojo.po.CheckIn;
import com.guest.mapper.CheckInMapper;
import com.guest.service.CheckInService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {
    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public List<CheckIn> getValidCheckIns(Timestamp fromTime, Timestamp toTime) {
        return checkInMapper.getValidCheckIns(fromTime,toTime);
    }

    @Override
    public int getNum(String roomId) {
        return checkInMapper.getNum(roomId);
    }

    @Override
    public List<CheckIn> getByIdCard(String idCard) {
        return checkInMapper.getByIdCard(idCard);
    }

    @Override
    public boolean removeByIdCard(String idCard) {
        return checkInMapper.removeByIdCard(idCard);
    }

    @Override
    public boolean removeByRoomId(String id) {
        return checkInMapper.removeByRoomId(id);
    }

    @Override
    public List<CheckIn> getValidCheckIns1(Timestamp fromTimeT, Timestamp toTimeT) {
        return checkInMapper.getValidCheckIns1(fromTimeT, toTimeT);
    }
}
