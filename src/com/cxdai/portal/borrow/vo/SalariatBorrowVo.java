package com.cxdai.portal.borrow.vo;

import com.cxdai.base.entity.Borrow;

public class SalariatBorrowVo extends Borrow {

	private String isGuarantyCB;
	private String oldBidPassword;

	public String getIsGuarantyCB() {
		return isGuarantyCB;
	}

	public void setIsGuarantyCB(String isGuarantyCB) {
		this.isGuarantyCB = isGuarantyCB;
	}

	public String getOldBidPassword() {
		return oldBidPassword;
	}

	public void setOldBidPassword(String oldBidPassword) {
		this.oldBidPassword = oldBidPassword;
	}

}