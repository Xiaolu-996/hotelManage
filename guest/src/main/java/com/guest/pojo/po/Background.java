package com.guest.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * <p>
 * 后台管理的账号
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Background extends Model<Background> {

    /**
     * 后台管理的工号
     */
    @TableId(value = "back_id")
    private String backId;

    /**
     * 后台登录密码
     */
    private String password;

}
