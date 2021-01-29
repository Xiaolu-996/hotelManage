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
 * 入住情况
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn extends Model<CheckIn> {

    /**
     * 入住情况的id
     */
    @TableId(value = "id")
    private Integer id;


    /**
     * 客户的id
     */
    private String guestIdCard;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 入住日期
     */
    private Timestamp fromTime;

    /**
     * 预计退房时间
     */
    private Timestamp toTime;

    /**
     * 状态，0代表已退房，1代表正在入住
     */
    private Integer state;

}
