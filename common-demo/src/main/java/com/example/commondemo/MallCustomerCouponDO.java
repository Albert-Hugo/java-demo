package com.example.commondemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户领优惠券表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallCustomerCouponDO {
    private String id;

    /**
     * 客户身份ID
     */
    private String identityId;

    /**
     * 客户主体id
     */
    private String customerId;

    /**
     * 微商城门店id
     */
    private String storeId;

    /**
     * 客户手机号码
     */
    private String customerMobile;

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 优惠券状态[1=未激活][2=已激活][3=已核销][4=退款申请中][5=已退款]
     */
    private Integer status;

    /**
     * 激活时间
     */
    private LocalDateTime activationDt;

    /**
     * 激活人id
     */
    private String activationId;

    /**
     * 激活人名称
     */
    private String activation;

    /**
     * 商场id
     */
    private String orgId;

    /**
     * 门店id
     */
    private String shopId;

    /**
     * 领优惠券时绑定的导购id
     */
    private String salesId;

    /**
     * 领优惠券时绑定的品牌
     */
    private String salesBrand;

    /**
     * 领优惠券时绑定的品类
     */
    private String salesCategory;

    /**
     * 优惠券支付订单ID
     */
    private String couponOrderId;

    /**
     * 优惠券使用核销时间
     */
    private LocalDateTime useDt;

    /**
     * 领取时间
     */
    private LocalDateTime createDt;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateDt;
}