package com.guest.service.serviceImpl;

import com.guest.pojo.po.Guest;
import com.guest.mapper.GuestMapper;
import com.guest.service.GuestService;
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
 * @since 2020-12-02
 */
@Service
public class GuestServiceImpl extends ServiceImpl<GuestMapper, Guest> implements GuestService {

    @Autowired
    GuestMapper guestMapper;

    @Override
    public List<Guest> getByIdCard(String idCard) {
        return guestMapper.getByIdCard(idCard);
    }

    @Override
    public List<Guest> getByContact(String contact) {
        return guestMapper.getByContact(contact);
    }

    @Override
    public List<Guest> getByName(String name) {
        return guestMapper.getByName(name);
    }
}
