package com.yun.lottery.service.activitystatus.operator;

import com.yun.lottery.service.dto.ConvertActivityStatusDTO;

/**
 * @author yun
 * @date 2025/4/28 15:33
 * @desciption:
 */
public abstract class AbstractActivityOperator {

    /**
     * 控制处理顺序
     *
     * @return
     */
    public abstract Integer sequence();

    /**
     * 是否需要转换
     *
     * @param convertActivityStatusDTO
     * @return
     */
    public abstract Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO);

    /**
     * 转换方法
     *
     * @param convertActivityStatusDTO
     * @return
     */
    public abstract Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO);

}