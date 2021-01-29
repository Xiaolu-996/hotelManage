package com.guest.mapper;

import com.guest.pojo.po.Guest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-12-02
 */
@Mapper
@Repository
public interface GuestMapper extends BaseMapper<Guest> {
    List<Guest> getByIdCard(String idCard);

    List<Guest> getByContact(String contact);

    List<Guest> getByName(String name);
}
