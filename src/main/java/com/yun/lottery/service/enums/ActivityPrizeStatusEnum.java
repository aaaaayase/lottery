package com.yun.lottery.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yun
 * @date 2025/4/26 20:55
 * @desciption:
 */
@AllArgsConstructor
@Getter
public enum ActivityPrizeStatusEnum {
    INIT(1, "初始化"),

    COMPLETED(2, "已被抽取");


    private final Integer code;

    private final String message;

    public static ActivityPrizeStatusEnum forName(String name) {
        for (ActivityPrizeStatusEnum activityPrizeStatusEnum : ActivityPrizeStatusEnum.values()) {
            if (activityPrizeStatusEnum.name().equalsIgnoreCase(name)) {
                return activityPrizeStatusEnum;
            }
        }
        return null;
    }
}