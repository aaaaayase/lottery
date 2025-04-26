package com.yun.lottery.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yun
 * @date 2025/4/26 16:01
 * @desciption:
 */
@Data
public class PrizeDTO {

    /**
     * 奖品Id
     */
    private Long prizeId;
    /**
     * 奖品名
     */
    private String name;

    /**
     * 图片索引
     */
    private String imageUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 描述
     */
    private String description;

}
