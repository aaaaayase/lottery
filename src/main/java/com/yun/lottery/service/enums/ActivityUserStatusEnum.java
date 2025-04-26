package com.yun.lottery.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yun
 * @date 2025/4/26 20:57
 * @desciption:
 */
@AllArgsConstructor
@Getter
public enum ActivityUserStatusEnum {

    INIT(1, "初始化"),

    COMPLETED(2, "已被抽取");


    private final Integer code;

    private final String message;

    public static ActivityUserStatusEnum forName(String name) {
        for (ActivityUserStatusEnum activityUserStatusEnum : ActivityUserStatusEnum.values()) {
            if (activityUserStatusEnum.name().equalsIgnoreCase(name)) {
                return activityUserStatusEnum;
            }
        }
        return null;
    }


}