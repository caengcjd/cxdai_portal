package com.cxdai.portal.borrow.vo;

import java.io.Serializable;

import com.cxdai.common.page.BaseCnd;


/**
 * @author WangQianJin
 * @title 业务员查询条件
 * @date 2015年8月17日
 */
public class BusinessCnd extends BaseCnd implements Serializable {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -1883307829868765036L;
	
	/**
	 * 用户名（来源于rocky_member表中USERNAME）
	 */
	private String username;
	/**
	 * 状态（数据字典）
	 */
	private Integer status;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}	
	
}
