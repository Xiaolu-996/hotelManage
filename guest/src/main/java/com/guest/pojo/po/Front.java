package com.guest.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 前台管理的账号
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Front extends Model<Front> {

    /**
     * 前台管理员的工号
     */
    @TableId(value = "front_id")
    private String frontId;

    /**
     * 前台管理员的姓名
     */
    private String name;

    /**
     * 前台管理的登录密码
     */
    private String password;

    /**
     * 前台管理员的电话
     */
    private String phone;

}
