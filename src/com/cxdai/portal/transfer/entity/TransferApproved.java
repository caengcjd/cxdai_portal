package com.cxdai.portal.transfer.entity;

import java.util.Date;

/**
 * <p>
 * Description:债权审核<br />
 * </p>
 * 
 * @title TransferApproved.java
 * @package com.cxdai.portal.transfer.entity
 * @author 邹理享
 * @version 0.1 2014年10月29日
 */
public class TransferApproved {

	private Integer id;// '主键ID',
	private Integer transferId;// '转让ID',
	private Integer status;// '审核状态，目前采用4步审核【审核不通过对应的状态取负值】（-1:草稿标
	// 0：待审核；1：初审通过，4：复审通过；）',
	private Integer verifyUser; // '审核人',
	private String verifyIp; // '审核IP',
	private String verifyMac; // '审核MAC',
	private Date verifyTime; // '审核时间',
	private String verifyRemark; // '审核备注',
	private String verifyUser2; // '复审审核人',
	private String verifyIp2; // '复审审核IP',
	private String verifyMac2; // '复审审核MAC',
	private Date verifyTime2; // '复审审核时间',
	private String verifyRemark2; // '复审审核备注',

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTransferId() {
		return transferId;
	}

	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(Integer verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getVerifyIp() {
		return verifyIp;
	}

	public void setVerifyIp(String verifyIp) {
		this.verifyIp = verifyIp;
	}

	public String getVerifyMac() {
		return verifyMac;
	}

	public void setVerifyMac(String verifyMac) {
		this.verifyMac = verifyMac;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public String getVerifyUser2() {
		return verifyUser2;
	}

	public void setVerifyUser2(String verifyUser2) {
		this.verifyUser2 = verifyUser2;
	}

	public String getVerifyIp2() {
		return verifyIp2;
	}

	public void setVerifyIp2(String verifyIp2) {
		this.verifyIp2 = verifyIp2;
	}

	public String getVerifyMac2() {
		return verifyMac2;
	}

	public void setVerifyMac2(String verifyMac2) {
		this.verifyMac2 = verifyMac2;
	}

	public Date getVerifyTime2() {
		return verifyTime2;
	}

	public void setVerifyTime2(Date verifyTime2) {
		this.verifyTime2 = verifyTime2;
	}

	public String getVerifyRemark2() {
		return verifyRemark2;
	}

	public void setVerifyRemark2(String verifyRemark2) {
		this.verifyRemark2 = verifyRemark2;
	}


}