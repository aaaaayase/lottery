package com.yun.lottery.controller.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: yun
 */
@Data
public class BaseUserInfoResult implements Serializable {

    /**
     * 人员id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 身份信息
     */
    private String identity;

}
