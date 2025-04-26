package com.yun.lottery.service.dto;

import com.yun.lottery.service.enums.ActivityStatusEnum;
import lombok.Data;

/**
 * @author yun
 * @date 2025/4/27 14:22
 * @desciption:
 */
@Data
public class ActivityDTO {
    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动状态
     */
    private ActivityStatusEnum status;

    /**
     * 活动是否有效
     *
     * @return
     */
    public Boolean valid() {
        return status.equals(ActivityStatusEnum.RUNNING);
    }
}