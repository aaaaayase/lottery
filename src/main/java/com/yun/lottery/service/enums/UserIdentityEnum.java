package com.yun.lottery.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yun
 * @date 2025/4/13 14:28
 * @desciption: 身份信息枚举
 */
@AllArgsConstructor
@Getter
public enum UserIdentityEnum {

    ADMIN("管理员"),
    NORMAL("普通用户");

    private final String message;

    public static UserIdentityEnum forName(String name) {
        for (UserIdentityEnum userIdentityEnum : UserIdentityEnum.values()) {
            if (userIdentityEnum.name().equalsIgnoreCase(name)) {
                return userIdentityEnum;
            }
        }

        return null;
    }
}
