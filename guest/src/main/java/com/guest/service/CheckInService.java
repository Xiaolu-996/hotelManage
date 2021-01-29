package com.guest.service;

import com.guest.pojo.po.CheckIn;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
public interface CheckInService extends IService<CheckIn> {

    List<CheckIn> getValidCheckIns(Timestamp fromTime, Timestamp toTime);

    int getNum(String roomId);

    List<CheckIn> getByIdCard(String idCard);

    boolean removeByIdCard(String idCard);

    boolean removeByRoomId(String id);

    List<CheckIn> getValidCheckIns1(Timestamp fromTimeT, Timestamp toTimeT);
}
