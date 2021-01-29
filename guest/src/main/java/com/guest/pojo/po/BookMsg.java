package com.guest.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 预定房间的信息
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMsg extends Model<BookMsg> {

    /**
     * 预定信息id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 预计入住时间
     */
    private Timestamp fromTime;

    /**
     * 预计退房时间
     */
    private Timestamp toTime;

    /**
     * 客户的身份证号
     */
    private String guestIdCard;

    /**
     * 预定的房间种类
     */
    private String rank;

    /**
     * 预定处理状态，0代表未处理，1代表已处理，11代表已入住(失效)
     */
    private Integer state;

    /**
     * 预定到的房间
     */
    private String resultRoom;

}
