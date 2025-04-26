package com.yun.lottery.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yun
 * @date 2025/4/26 15:59
 * @desciption:
 */
@Data
public class PageListDTO<T> {

    /**
     * 总量
     */
    private Integer total;

    /**
     * 当前页列表
     */
    private List<T> records;


    public PageListDTO(Integer total, List<T> records) {
        this.total = total;
        this.records = records;
    }


}