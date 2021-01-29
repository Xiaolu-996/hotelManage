package com.guest.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 消费类型
 * </p>
 *
 * @author 张雪萍
 * @since 2020-12-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostType extends Model<CostType> {


    /**
     * 消费类型的id
     */
    private Integer id;

    /**
     * 消费项目的名称
     */
    private String name;

    /**
     * 金额
     */
    private Double money;



}
