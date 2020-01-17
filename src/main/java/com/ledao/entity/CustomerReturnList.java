package com.ledao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 客户退货单实体
 *
 * @author LeDao
 * @company
 * @create 2020-01-17 15:02
 */
@Data
public class CustomerReturnList {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 客户退货单号
     */
    private String customerReturnNumber;
    /**
     * 客户
     */
    private Customer customer;
    /**
     * 客户退货日期
     */
    private Date customerReturnDate;
    /**
     * 起始日期 搜索用到
     */
    private Date bCustomerReturnDate;
    /**
     * 结束日期 搜索用到
     */
    private Date eCustomerReturnDate;
    /**
     * 应付金额
     */
    private float amountPayable;
    /**
     * 实付金额
     */
    private float amountPaid;
    /**
     * 交易状态 1 已付 2 未付
     */
    private Integer state;
    /**
     * 操作员
     */
    private User user;
    /**
     * 备注
     */
    private String remarks;
}
