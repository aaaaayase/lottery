package com.yun.lottery.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yun
 * @date 2025/4/26 20:26
 * @desciption:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityDO extends BaseDO {

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
    private String status;


}