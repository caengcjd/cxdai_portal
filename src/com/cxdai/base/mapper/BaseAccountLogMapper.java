package com.cxdai.base.mapper;

import com.cxdai.base.entity.AccountLog;

/**
 * <p>
 * Description:账户历史记录数据访问类<br />
 * </p>
 * @title BaseAccountLogMapper.java
 * @package com.cxdai.base.mapper
 * @author justin.xu
 * @version 0.1 2014年4月25日
 */
public interface BaseAccountLogMapper {
	/**
	 * <p>
	 * Description:插入记录,返回新增的主键ID<br />
	 * </p>
	 * @author justin.xu
	 * @version 0.1 2014年4月15日
	 * @param accountLog
	 * @return
	 * @throws Exception int
	 */
	public int insertEntity(AccountLog accountLog) throws Exception;

	/**
	 * <p>
	 * Description:根据id查询<br />
	 * </p>
	 * @author justin.xu
	 * @version 0.1 2014年4月15日
	 * @param id
	 * @return
	 * @throws Exception AccountLog
	 */
	public AccountLog queryById(Integer id) throws Exception;

	/**
	 * 查询充值记录是否入账
	 * <p>
	 * Description:这里写描述<br />
	 * </p>
	 * @author huangpin
	 * @version 0.1 2015年8月24日
	 * @param borrowId
	 * @return int
	 */
	public int queryRecharge(Integer borrowId);

	/**
	 * <p>
	 * Description:更新对象<br />
	 * </p>
	 * @author justin.xu
	 * @version 0.1 2014年4月15日
	 * @param accountLog
	 * @return
	 * @throws Exception int
	 */
	public int updateEntity(AccountLog accountLog) throws Exception;

}
