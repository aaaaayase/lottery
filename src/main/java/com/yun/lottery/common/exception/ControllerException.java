package com.yun.lottery.common.exception;

import com.yun.lottery.common.errorcode.ErrorCode;
import lombok.*;

/**
 * @author yun
 * @date 2025/4/12 15:56
 * @desciption: Controller层异常类
 */
@Data
@AllArgsConstructor
// 用于支持序列化
@NoArgsConstructor
@Setter
@Getter
// 不写他可能会出现问题 因为data注解是会忽略父类的属性的
@EqualsAndHashCode(callSuper = true)
public class ControllerException extends RuntimeException {
    /**
     * 异常码
     * @see com.yun.lottery.common.errorcode.ControllerErrorCodeConstants
     */
    private Integer code;
    private String message;

    public ControllerException(ErrorCode errorCode) {
        code = errorCode.getCode();
        message = errorCode.getMsg();
    }

}
