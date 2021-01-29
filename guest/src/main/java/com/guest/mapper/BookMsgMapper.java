package com.guest.mapper;

import com.guest.pojo.po.BookMsg;
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
public interface BookMsgMapper extends BaseMapper<BookMsg> {
    List<BookMsg> getBookMsgByIdCard(String idCard);
    List<BookMsg> getBookMsgByTime(Timestamp fromTime, Timestamp toTime);
    boolean removeByIdCard(String idCard);
    boolean removeByResultRoom(String id);
    List<BookMsg> getBookMsgByTime1(Timestamp fromTime, Timestamp toTime);

}
