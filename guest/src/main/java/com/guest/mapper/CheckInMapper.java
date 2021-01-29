package com.guest.mapper;

import com.guest.pojo.po.CheckIn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Mapper
@Repository
public interface CheckInMapper extends BaseMapper<CheckIn> {
    List<CheckIn> getValidCheckIns(Timestamp fromTime, Timestamp toTime);

    int getNum(String roomId);

    List<CheckIn> getByIdCard(String idCard);
    boolean removeByIdCard(String idCard);
    boolean removeByRoomId(String id);
    List<CheckIn> getValidCheckIns1(Timestamp fromTimeT, Timestamp toTimeT);
}
