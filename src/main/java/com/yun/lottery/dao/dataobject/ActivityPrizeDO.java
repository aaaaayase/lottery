package com.yun.lottery.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yun
 * @date 2025/4/26 21:19
 * @desciption:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPrizeDO extends BaseDO {

    /**
     * 关联的活动id
     */
    private Long activityId;
    /**
     * 关联的奖品id
     */
    private Long prizeId;
    /**
     * 奖品数量
     */
    private Long prizeAmount;
    /**
     * 奖品等奖
     */
    private String prizeTiers;
    /**
     * 奖品状态
     */
    private String status;


}
