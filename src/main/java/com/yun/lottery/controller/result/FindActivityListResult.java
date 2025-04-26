package com.yun.lottery.controller.result;

/**
 * @author yun
 * @date 2025/4/26 15:54
 * @desciption:
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class FindActivityListResult implements Serializable {

    /**
     * 总量
     */
    private Integer total;

    /**
     * 当前列表
     */
    private List<ActivityInfo> records;

    @Data
    public static class ActivityInfo implements Serializable {

        /**
         * 奖品id
         */
        private Long activityId;

        /**
         * 名称
         */
        private String activityName;

        /**
         * 描述
         */
        private String description;

        /**
         * 活动是否有效
         */
        private Boolean valid;



    }

}
