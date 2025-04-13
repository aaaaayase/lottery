package com.yun.lottery.common.exception;

import com.yun.lottery.common.errorcode.ErrorCode;
import lombok.*;

/**
 * @author yun
 * @date 2025/4/12 15:56
 * @desciption: Service层异常类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
    /**
     * 异常码
     * @see com.yun.lottery.common.errorcode.ServiceErrorCodeConstants
     */
    private Integer code;
    private String message;

    public ServiceException(ErrorCode errorCode) {
        code = errorCode.getCode();
        message = errorCode.getMsg();
    }

}
