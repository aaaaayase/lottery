package com.yun.lottery.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yun
 * @date 2025/4/12 15:43
 * @desciption: 错误码
 */
@Data
@AllArgsConstructor
@Setter
@Getter
public class ErrorCode {

    private int code;
    private String msg;

}
