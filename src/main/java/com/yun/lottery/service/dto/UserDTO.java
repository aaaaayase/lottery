package com.yun.lottery.service.dto;

import com.yun.lottery.dao.dataobject.Encrypt;
import com.yun.lottery.service.enums.UserIdentityEnum;
import lombok.Data;

/**
 * @author yun
 * @date 2025/4/26 10:04
 * @desciption:
 */
@Data
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private UserIdentityEnum identity;
}
