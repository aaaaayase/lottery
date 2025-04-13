package com.yun.lottery.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author yun
 * @date 2025/4/13 13:08
 * @desciption:
 */
@Data
public class UserRegisterParam {

    @NotBlank(message = "姓名不能为空！")
    private String name;
    @NotBlank(message = "邮箱不能为空！")
    private String mail;
    @NotBlank(message = "电话不能为空！")
    private String phoneNumber;
    private String password;
    @NotBlank(message = "身份信息不能为空！")
    private String identity;
}
