package com.yun.lottery.controller.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yun
 * @date 2025/4/26 15:52
 * @desciption:
 */
@Data
public class PageParam implements Serializable {

    /**
     * 当前页
     */
    private Integer currentPage = 1;

    /**
     * 当前页数量
     */
    private Integer pageSize = 10;

    /**
     * 获取偏移量
     *
     * @return
     */
    public Integer offset() {
        return (currentPage-1) * pageSize;
    }


}

