package com.guest.service.serviceImpl;

import com.guest.pojo.po.BookMsg;
import com.guest.mapper.BookMsgMapper;
import com.guest.service.BookMsgService;
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
public class BookMsgServiceImpl extends ServiceImpl<BookMsgMapper, BookMsg> implements BookMsgService {
    @Autowired
    private BookMsgMapper bookMsgMapper;

    @Override
    public List<BookMsg> getBookMsgByIdCard(String idCard) {
        return bookMsgMapper.getBookMsgByIdCard(idCard);
    }

    @Override
    public List<BookMsg> getBookMsgByTime(Timestamp fromTime, Timestamp toTime) {
        return bookMsgMapper.getBookMsgByTime(fromTime,toTime);
    }

    @Override
    public boolean removeByIdCard(String idCard) {
        return bookMsgMapper.removeByIdCard(idCard);
    }

    @Override
    public boolean removeByResultRoom(String id) {
        return bookMsgMapper.removeByResultRoom(id);
    }

    @Override
    public List<BookMsg> getBookMsgByTime1(Timestamp fromTimeT, Timestamp toTimeT) {
        return bookMsgMapper.getBookMsgByTime(fromTimeT,toTimeT);
    }
}
