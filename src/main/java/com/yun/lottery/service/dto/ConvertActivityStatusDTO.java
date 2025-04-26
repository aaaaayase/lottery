package com.yun.lottery.service.dto;

import com.yun.lottery.service.enums.ActivityPrizeStatusEnum;
import com.yun.lottery.service.enums.ActivityStatusEnum;
import com.yun.lottery.service.enums.ActivityUserStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * @author yun
 * @date 2025/4/28 15:23
 * @desciption:
 */
@Data
public class ConvertActivityStatusDTO {

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动目标状态
     */
    private ActivityStatusEnum targetActivityStatus;

    /**
     * 奖品id
     */
    private Long prizeId;

    /**
     * 奖品目标状态
     */
    private ActivityPrizeStatusEnum targetPrizeStatus;

    /**
     * 人员id列表
     */
    private List<Long> userIds;

    /**
     * 人员目标状态
     */
    private ActivityUserStatusEnum targetUserStatus;

}