package com.yun.lottery.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yun
 * @date 2025/4/26 19:15
 * @desciption:
 */
@Data
public class CreatePrizeByActivityParam implements Serializable {

    /**
     * 活动关联的奖品id
     */
    @NotNull(message = "活动关联的奖品id不能为空！")
    private Long prizeId;
    /**
     * 奖品数量
     */
    @NotNull(message = "奖品数量不能为空！")
    private Long prizeAmount;
    /**
     * 奖品等奖
     */
    @NotBlank(message = "奖品等奖不能为空！")
    private String prizeTiers;


}