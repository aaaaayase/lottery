package com.yun.lottery.controller;

import com.yun.lottery.common.errorcode.ControllerErrorCodeConstants;
import com.yun.lottery.common.exception.ControllerException;
import com.yun.lottery.common.pojo.CommonResult;
import com.yun.lottery.common.utils.JacksonUtil;
import com.yun.lottery.controller.param.CreateActivityParam;
import com.yun.lottery.controller.param.PageParam;
import com.yun.lottery.controller.result.CreateActivityResult;
import com.yun.lottery.controller.result.FindActivityListResult;
import com.yun.lottery.controller.result.GetActivityDetailResult;
import com.yun.lottery.service.ActivityService;
import com.yun.lottery.service.dto.ActivityDTO;
import com.yun.lottery.service.dto.ActivityDetailDTO;
import com.yun.lottery.service.dto.CreateActivityDTO;
import com.yun.lottery.service.dto.PageListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author yun
 * @date 2025/4/26 18:58
 * @desciption:
 */
@RestController
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // user 45 44
    // prize 18 19

    /**
     * 创建活动
     *
     * @param param
     * @return
     */
    @RequestMapping("/activity/create")
    public CommonResult<CreateActivityResult> createActivity(
            @Validated @RequestBody CreateActivityParam param) {
        log.info("createActivity CreateActivityParam:{}",
                JacksonUtil.writeValueAsString(param));
        return CommonResult.success(
                convertToCreateActivityResult(activityService.createActivity(param)));
    }

    @RequestMapping("/activity/find-list")
    public CommonResult<FindActivityListResult> findActivityList(PageParam param) {
        log.info("findActivityList PageParam:{}",
                JacksonUtil.writeValueAsString(param));
        return CommonResult.success(
                convertToFindActivityListResult(
                        activityService.findActivityList(param)));
    }
    private FindActivityListResult convertToFindActivityListResult(PageListDTO<ActivityDTO> activityList) {
        if (null == activityList) {
            throw new ControllerException(ControllerErrorCodeConstants.FIND_ACTIVITY_LIST_ERROR);
        }
        FindActivityListResult result = new FindActivityListResult();
        result.setTotal(activityList.getTotal());
        result.setRecords(
                activityList.getRecords()
                        .stream()
                        .map(activityDTO -> {
                            FindActivityListResult.ActivityInfo activityInfo = new FindActivityListResult.ActivityInfo();
                            activityInfo.setActivityId(activityDTO.getActivityId());
                            activityInfo.setActivityName(activityDTO.getActivityName());
                            activityInfo.setDescription(activityDTO.getDescription());
                            activityInfo.setValid(activityDTO.valid());
                            return activityInfo;
                        }).collect(Collectors.toList())
        );
        return result;
    }
    @RequestMapping("/activity-detail/find")
    public CommonResult<GetActivityDetailResult> getActivityDetail(Long activityId) {
        log.info("getActivityDetail activityId:{}", activityId);
        ActivityDetailDTO detailDTO = activityService.getActivityDetail(activityId);
        return CommonResult.success(convertToGetActivityDetailResult(detailDTO));
    }

    private GetActivityDetailResult convertToGetActivityDetailResult(ActivityDetailDTO detailDTO) {
        if (null == detailDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.GET_ACTIVITY_DETAIL_ERROR);
        }

        GetActivityDetailResult result = new GetActivityDetailResult();
        result.setActivityId(detailDTO.getActivityId());
        result.setActivityName(detailDTO.getActivityName());
        result.setDescription(detailDTO.getDesc());
        result.setValid(detailDTO.valid());
        // 抽奖顺序：一等奖、二、三
        result.setPrizes(
                detailDTO.getPrizeDTOList().stream()
                        .sorted(Comparator.comparingInt(prizeDTO -> prizeDTO.getTiers().getCode()))
                        .map(prizeDTO -> {
                            GetActivityDetailResult.Prize prize = new GetActivityDetailResult.Prize();
                            prize.setPrizeId(prizeDTO.getPrizeId());
                            prize.setName(prizeDTO.getName());
                            prize.setImageUrl(prizeDTO.getImageUrl());
                            prize.setPrice(prizeDTO.getPrice());
                            prize.setDescription(prizeDTO.getDescription());
                            prize.setPrizeTierName(prizeDTO.getTiers().getMessage());
                            prize.setPrizeAmount(prizeDTO.getPrizeAmount());
                            prize.setValid(prizeDTO.valid());
                            return prize;
                        }).collect(Collectors.toList())
        );
        result.setUsers(
                detailDTO.getUserDTOList().stream()
                        .map(userDTO -> {
                            GetActivityDetailResult.User user = new GetActivityDetailResult.User();
                            user.setUserId(userDTO.getUserId());
                            user.setUserName(userDTO.getUserName());
                            user.setValid(userDTO.valid());
                            return user;
                        }).collect(Collectors.toList())
        );
        return result;
    }



    private CreateActivityResult convertToCreateActivityResult(CreateActivityDTO createActivityDTO) {
        if (null == createActivityDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);
        }
        CreateActivityResult result = new CreateActivityResult();
        result.setActivityId(createActivityDTO.getActivityId());
        return result;
    }


}
