package com.guest.service;

import com.guest.pojo.po.BookMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
public interface BookMsgService extends IService<BookMsg> {


    List<BookMsg> getBookMsgByIdCard(String idCard);

    List<BookMsg> getBookMsgByTime(Timestamp fromTime, Timestamp toTime);

    boolean removeByIdCard(String idCard);

    boolean removeByResultRoom(String id);

    List<BookMsg> getBookMsgByTime1(Timestamp fromTimeT, Timestamp toTimeT);
}
