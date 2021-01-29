package com.guest.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 房间信息
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room extends Model<Room> {

    /**
     * 房间编号
     */
    @TableId(value = "room_id")
    private String roomId;

    /**
     * 房间的大小，以平方米为单位
     */
    private Double size;

    /**
     * 级别，分A,B,C,D级，级别依次降低
     */
    private String rank;

    /**
     * 租金，单位是人民币元
     */
    private Double rent;

    /**
     * 入住定金，单位是人民币元
     */
    private Double earnest;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 地理位置
     */
    private String position;

}
