package cn.skyln.web.service;

import cn.skyln.model.CouponRecordMessage;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.DO.CouponTaskDO;
import cn.skyln.web.model.DTO.CouponDTO;
import cn.skyln.web.model.DTO.LockCouponRecordDTO;
import cn.skyln.web.model.VO.CouponRecordVO;

import java.util.Map;

/**
 * @author lamella
 * @description CouponRecordService TODO
 * @since 2024-04-03 19:22
 */
public interface CouponRecordService {

    /**
     * 分页查询优惠券
     *
     * @param page     第几页
     * @param size     一页显示几条
     * @param useState 查询类型
     * @return Map
     */
    Map<String, Object> pageCouponActivity(int page, int size, String useState);

    /**
     * 根据ID查询优惠券记录详情
     *
     * @param couponRecordId 记录ID
     * @return CouponRecordVO
     */
    CouponRecordVO getOneById(String couponRecordId);

    /**
     * 锁定优惠券
     *
     * @param lockCouponRecordDTO 锁定优惠券请求对象
     * @return JsonData
     */
    JsonData lockCouponRecord(LockCouponRecordDTO lockCouponRecordDTO);

    /**
     * 解锁优惠券记录
     *
     * @param couponRecordMessage CouponRecordMessage
     * @return 解锁状态
     */
    boolean releaseCouponRecord(CouponRecordMessage couponRecordMessage);

    /**
     * 订单不存在，或者订单被取消，确认消息，修改task状态为CANCEL，恢复优惠券使用记录为NEW
     *
     * @param couponTaskDO CouponTaskDO
     */
    void cancelCouponRecord(CouponTaskDO couponTaskDO);

    /**
     * 订单不存在，或者订单被取消，确认消息，修改task状态为CANCEL，恢复优惠券使用记录为NEW
     *
     * @param taskId taskId
     */
    void cancelCouponRecord(String taskId);

    /**
     * 根据ID列表获取优惠券详情并锁定优惠券
     *
     * @param couponDTO CouponDTO
     * @return JsonData
     */
    JsonData queryUserCouponRecord(CouponDTO couponDTO);
}
