package com.yun.lottery.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yun
 * @date 2025/4/13 16:14
 * @desciption:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO {
    private String userName;
    private String email;
    private Encrypt phoneNumber;
    private String password;
    private String identity;
}
