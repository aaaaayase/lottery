package com.yun.lottery.controller;

import com.yun.lottery.common.errorcode.ControllerErrorCodeConstants;
import com.yun.lottery.common.exception.ControllerException;
import com.yun.lottery.common.pojo.CommonResult;
import com.yun.lottery.common.utils.JacksonUtil;
import com.yun.lottery.controller.param.CreatePrizeParam;
import com.yun.lottery.controller.param.PageParam;
import com.yun.lottery.controller.result.FindPrizeListResult;
import com.yun.lottery.service.PictureService;
import com.yun.lottery.service.PrizeService;
import com.yun.lottery.service.dto.PageListDTO;
import com.yun.lottery.service.dto.PrizeDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

/**
 * @author: yibo
 */
@Slf4j
@RestController
public class PrizeController {

    @Autowired
    private PictureService pictureService;
    @Autowired
    private PrizeService prizeService;

    @RequestMapping("/pic/upload")
    public String uploadPic(MultipartFile file) {
        return pictureService.savePicture(file);
    }

    /**
     * 创建奖品
     *  RequestPart:用于接收表单数据的 multipart/form-data
     *
     * @param param
     * @param picFile
     * @return
     */
    @RequestMapping("/prize/create")
    public CommonResult<Long> createPrize(@Valid @RequestPart("param") CreatePrizeParam param,
                                          @RequestPart("prizePic") MultipartFile picFile) {
        log.info("createPrize CreatePrizeParam:{}",
                JacksonUtil.writeValueAsString(param));
        return CommonResult.success(
                prizeService.createPrize(param, picFile));
    }

    @RequestMapping("/prize/find-list")
    public CommonResult<FindPrizeListResult> findPrizeList(PageParam param) {
        log.info("findPrizeList PageParam:{}",
                JacksonUtil.writeValueAsString(param));
        PageListDTO<PrizeDTO> pageListDTO = prizeService.findPrizeList(param);
        return CommonResult.success(convertToFindPrizeListResult(pageListDTO));

    }

    private FindPrizeListResult convertToFindPrizeListResult(PageListDTO<PrizeDTO> pageListDTO) {
        if (null == pageListDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.FIND_PRIZE_LIST_ERROR);
        }

        FindPrizeListResult result = new FindPrizeListResult();
        result.setTotal(pageListDTO.getTotal());
        result.setRecords(
                pageListDTO.getRecords().stream()
                        .map(prizeDTO -> {
                            FindPrizeListResult.PrizeInfo prizeInfo = new FindPrizeListResult.PrizeInfo();
                            prizeInfo.setPrizeId(prizeDTO.getPrizeId());
                            prizeInfo.setPrizeName(prizeDTO.getName());
                            prizeInfo.setDescription(prizeDTO.getDescription());
                            prizeInfo.setImageUrl(prizeDTO.getImageUrl());
                            prizeInfo.setPrice(prizeDTO.getPrice());
                            return prizeInfo;
                        }).collect(Collectors.toList())
        );
        return result;
    }


}
