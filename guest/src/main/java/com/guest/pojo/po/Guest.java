package com.guest.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 客户
 * </p>
 *
 * @author 张雪萍
 * @since 2020-12-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest extends Model<Guest> {

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


}
