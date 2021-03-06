package com.cxdai.base.mapper;

import org.springframework.stereotype.Repository;

import com.cxdai.base.entity.BankInfo;

/**
 * <p>
 * Description:银行信息数据访问类<br />
 * </p>
 * 
 * @title BaseBankInfoMapper.java
 * @package com.cxdai.base.mapper
 * @author justin.xu
 * @version 0.1 2014年6月24日
 */
public interface BaseBankInfoMapper {
	/**
	 * <p>
	 * Description:插入记录,返回新增的主键ID<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年4月15日
	 * @param bankInfo
	 * @return
	 * @throws Exception
	 *             int
	 */
	public int insertEntity(BankInfo bankInfo) throws Exception;

	/**
	 * <p>
	 * Description:根据id查询<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年4月15日
	 * @param id
	 * @return
	 * @throws Exception
	 *             Account
	 */
	public BankInfo queryById(Integer id) throws Exception;

	/**
	 * <p>
	 * Description:更新对象<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年4月15日
	 * @param account
	 * @return
	 * @throws Exception
	 *             int
	 */
	public int updateEntity(BankInfo bankInfo) throws Exception;

}
