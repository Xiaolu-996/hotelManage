package com.guest.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 客户的身份信息和入住信息
 * </p>
 *
 * @author 张雪萍
 * @since 2020-12-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestMsg {
    /**
     * 客户的身份证号
     */
    @TableId("id_card")
    private String idCard;

    /**
     * 客户的姓名
     */
    private String name;

    /**
     * 客户的联系方式
     */
    private String contact;

    /**
     * 如果是1表示已入住，如果是0表示已预定，如果是-1表示已退房
     */
    private Integer state;

    /**
     * 房间号
     */
    private String roomId;
}
