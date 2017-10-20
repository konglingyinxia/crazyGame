package com.crazy.web.model.vo;

import java.math.BigDecimal;

public class RunSummaryVo {

	/**
	 * 下级玩家数量
	 */
	private Integer subCount;

	private BigDecimal trtProfit;// 分润剩余总额

	private BigDecimal ppAmount;// 待审批分润金额

	private BigDecimal amountPaid;// 已提现金额

	public Integer getSubCount() {
		return subCount;
	}

	public void setSubCount(Integer subCount) {
		this.subCount = subCount;
	}

	public BigDecimal getTrtProfit() {
		return trtProfit;
	}

	public void setTrtProfit(BigDecimal trtProfit) {
		this.trtProfit = trtProfit;
	}

	public BigDecimal getPpAmount() {
		return ppAmount;
	}

	public void setPpAmount(BigDecimal ppAmount) {
		this.ppAmount = ppAmount;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

}
