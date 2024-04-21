package cn.skyln.enums;

import lombok.Getter;

/**
 * @Author lamella
 * @Date 2022/11/26/20:03
 * @Description 状态码定义约束，共6位数，前三位代表服务，后3位代表接口
 */
@Getter
public enum BizCodeEnum {
    /**
     * 成功操作码
     */
    SEARCH_SUCCESS(0, "数据查询成功"),
    OPERATE_SUCCESS(0, "操作成功！数据更新可能延迟，如刷新后未显示变更，请耐心等待"),
    SEND_CODE_SUCCESS(0, "验证码发送成功"),
    LOGIN_SUCCESS(0, "登录成功"),

    /**
     * 通用操作码
     */
    OPS_REPEAT(100001, "重复操作"),
    OPS_NETWORK_ADDRESS_ERROR(100002, "网络地址错误"),
    SYSTEM_ERROR(100003, "系统错误，请稍后重试，或联系系统管理员。"),
    SYSTEM_NO_NACOS_INSTANCE(100004, "没有NACOS服务，请稍后重试，或联系系统管理员。"),

    /**
     * 数据
     */
    DATA_SPECIFICATION(101001, "数据不符合规范，请检查后重新输入。"),
    DATA_ERROR(101002, "错误数据"),
    DATA_NOT_EXIST(101003, "数据不存在，请检查后重新输入。"),
    DATA_NOT_FOUND_ERROR(101004, "数据不存在或数据异常。"),

    /**
     * 账号
     */
    ACCOUNT_REPEAT(110001, "账号已经存在"),
    ACCOUNT_LOGIN_ERROR(110002, "账号或密码错误，请重试，或联系系统管理员"),
    ACCOUNT_PERMISSION_ERROR(110003, "您无权访问该系统资源，请联系系统管理员"),
    ACCOUNT_REGISTER_SUCCESS(110004, "账号注册成功"),
    ACCOUNT_REGISTER_PWD_ERROR(110005, "两次输入的密码不同，请检查后再次输入"),
    ACCOUNT_REGISTER_ERROR(110006, "账号注册失败"),
    ACCOUNT_NOT_EXIST_ERROR(110007, "输入的账号为空"),
    ACCOUNT_PWD_NOT_EXIST_ERROR(110008, "输入的密码为空"),
    ACCOUNT_UNLOGIN_ERROR(110009, "登录状态失效或当前未登录，请重新登录"),
    ACCOUNT_ACCESS_TOKEN_EXPIRED(110010, "当前token过期但不需要重新登录"),
    ACCOUNT_ACCESS_TOKEN_EXPIRED_RELOGIN(110011, "当前token过期且需要重新登录"),
    ACCOUNT_SAFE_MODE_RELOGIN(110012, "两次登录的IP地址不同，请重新登录"),
    ACCOUNT_UNREGISTER(110013, "账号不存在"),

    /**
     * 地址相关
     */
    ADDRESS_NOT_EXIT(120001, "收货地址不存在"),
    ADDRESS_ADD_FAIL(120002, "新增收货地址失败"),
    ADDRESS_DEL_FAIL(120003, "删除收货地址失败"),
    ADDRESS_UPD_FAIL(120004, "更新收货地址失败"),


    /**
     * 验证码
     */
    CODE_TO_ERROR(130001, "接收号码不合规"),
    CODE_LIMITED(130002, "验证码发送过快"),
    CODE_ERROR(130003, "验证码错误"),
    CODE_TYPE_ERROR(130004, "验证码类型不存在"),
    CODE_SEND_NUM_ERROR(130005, "验证码一天只能发送5次"),
    CODE_CAPTCHA_ERROR(130006, "图形验证码错误"),

    /**
     * 优惠券
     */
    COUPON_CONDITION_ERROR(200001, "优惠券条件错误"),
    COUPON_UNAVAILABLE(200002, "没有可用的优惠券"),
    COUPON_CANNOT_USED(200003, "存在不可用的优惠券"),
    COUPON_NO_EXITS(200004, "优惠券不存在"),
    COUPON_NO_STOCK(200005, "优惠券库存不足"),
    COUPON_OUT_OF_LIMIT(200006, "优惠券领取超过限制次数"),
    COUPON_OUT_OF_TIME(200007, "优惠券不在领取时间范围"),
    COUPON_GET_FAIL(200008, "优惠券领取失败"),
    COUPON_RECORD_LOCK_FAIL(200009, "优惠券锁定失败"),

    /**
     * 订单
     */
    ORDER_CONFIRM_COUPON_FAIL(210001, "创建订单-优惠券使用失败,不满足价格条件"),
    ORDER_CONFIRM_PRICE_FAIL(210002, "创建订单-验价失败"),
    ORDER_CONFIRM_LOCK_PRODUCT_FAIL(210003, "创建订单-商品库存锁定失败"),
    ORDER_CONFIRM_ADD_STOCK_TASK_FAIL(210004, "创建订单-新增商品库存锁定任务"),
    ORDER_CONFIRM_TOKEN_NOT_EXIST(210008, "订单令牌缺少"),
    ORDER_CONFIRM_TOKEN_EQUAL_FAIL(210009, "订单令牌不正确"),
    ORDER_CONFIRM_NOT_EXIST(210010, "订单不存在"),
    ORDER_CONFIRM_CART_ITEM_NOT_EXIST(210011, "购物车商品项不存在"),

    /**
     * 支付
     */
    PAY_ORDER_FAIL(220001, "创建支付订单失败"),
    PAY_ORDER_CALLBACK_SIGN_FAIL(220002, "支付订单回调验证签失败"),
    PAY_ORDER_CALLBACK_NOT_SUCCESS(220003, "支付回调订单更新失败"),
    PAY_ORDER_NOT_EXIST(220005, "订单不存在"),
    PAY_ORDER_STATE_ERROR(220006, "订单状态不正常"),
    PAY_ORDER_PAY_TIMEOUT(220007, "订单支付超时"),

    /**
     * 商品相关
     */
    PRODUCT_NOT_EXIT(230001, "商品不存在"),
    PRODUCT_ADD_FAIL(230002, "新增商品失败"),
    PRODUCT_DEL_FAIL(230003, "删除商品失败"),
    PRODUCT_UPD_FAIL(230004, "更新商品失败"),
    /**
     * 流量包操作
     */
    TRAFFIC_FREE_NOT_EXIST(231001, "免费流量包不存在，联系客服"),
    TRAFFIC_REDUCE_FAIL(231002, "流量不足，扣减失败"),
    TRAFFIC_EXCEPTION(231003, "流量包数据异常,用户无流量包"),

    /**
     * 购物车相关
     */
    CART_NOT_EXIT(240001, "购物车中没有商品"),
    CART_UPD_NUM_FAIL(240002, "请输入大于0的商品数量"),

    /**
     * 轮播图相关
     */
    BANNER_NOT_EXIT(250001, "轮播图不存在"),
    BANNER_ADD_FAIL(250002, "新增轮播图失败"),
    BANNER_DEL_FAIL(250003, "删除轮播图失败"),
    BANNER_UPD_FAIL(250004, "更新轮播图失败"),

    /**
     * 短链分组
     */
    GROUP_REPEAT(300001, "分组名重复"),
    GROUP_OPER_FAIL(300002, "分组名操作失败"),
    GROUP_NOT_EXIST(300003, "分组不存在"),

    /**
     * 短链
     */
    SHORT_LINK_NOT_EXIST(310001, "短链不存在"),

    /**
     * 云测项目
     */
    PROJECT_NOT_EXIT(400001, "项目不存在"),

    /**
     * 云测环境
     */
    ENVIRONMENT_NOT_EXIT(410001, "环境不存在"),

    /**
     * 流控操作
     */
    CONTROL_FLOW_EXCEPTION(500001, "限流异常，已触发流量控制，请稍后再次尝试。"),
    CONTROL_DEGRADE_EXCEPTION(500002, "降级异常，已触发流量控制，请稍后再次尝试。"),
    CONTROL_PARAM_FLOW_EXCEPTION(500003, "参数限流异常，已触发流量控制，请稍后再次尝试。"),
    CONTROL_SYSTEM_BLOCK_EXCEPTION(500004, "系统负载异常，已触发流量控制，请稍后再次尝试。"),
    CONTROL_AUTHORITY_EXCEPTION(500005, "授权异常，已触发流量控制，请稍后再次尝试。"),


    /**
     * 文件相关
     */
    FILE_UPLOAD_USER_IMG_FAIL(700001, "用户头像文件上传失败");

    private final String message;

    private final int code;

    BizCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

