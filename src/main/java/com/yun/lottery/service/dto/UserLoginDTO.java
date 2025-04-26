package com.yun.lottery.service.dto;

import com.yun.lottery.service.enums.UserIdentityEnum;
import lombok.Data;

/**
 * @author yun
 * @date 2025/4/25 22:42
 * @desciption:
 */
@Data
public class UserLoginDTO {

    private String token;

    private UserIdentityEnum identity;
}
