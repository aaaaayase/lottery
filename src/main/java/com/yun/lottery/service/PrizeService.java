package com.yun.lottery.service;


import com.yun.lottery.controller.param.CreatePrizeParam;
import com.yun.lottery.controller.param.PageParam;
import com.yun.lottery.service.dto.PageListDTO;
import com.yun.lottery.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: yun
 */
public interface PrizeService {

    /**
     * 创建单个奖品
     *
     * @param param 奖品属性
     * @param picFile  上传的奖品图
     * @return  奖品id
     */
    Long createPrize(CreatePrizeParam param, MultipartFile picFile);


    /**
     * 翻页查询列表
     *
     * @param param
     * @return
     */
    PageListDTO<PrizeDTO> findPrizeList(PageParam param);
}
