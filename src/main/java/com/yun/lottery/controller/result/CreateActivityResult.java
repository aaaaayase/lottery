package com.yun.lottery.controller.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yun
 * @date 2025/4/26 19:10
 * @desciption:
 */
@Data
public class CreateActivityResult implements Serializable {

    /**
     * 创建的活动id
     */
    private Long activityId;

}