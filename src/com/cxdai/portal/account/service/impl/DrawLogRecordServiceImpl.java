package com.cxdai.portal.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxdai.common.page.Page;
import com.cxdai.portal.account.mapper.DrawLogRecordMapper;
import com.cxdai.portal.account.service.DrawLogRecordService;
import com.cxdai.portal.account.vo.TurnDrawLogVO;
import com.cxdai.utils.DateUtils;

@Service
public class DrawLogRecordServiceImpl implements DrawLogRecordService {

	@Autowired
	private DrawLogRecordMapper drawLogRecordMapper;
	@Override
	public Page queryDrawPageListByCnd(double userId,String startTime,String endTime, Page page) {
		
			if(startTime != null && !"".equals(startTime)){
				startTime = com.cxdai.utils.DateUtils.date2TimeStamp(startTime);
			}
			if(endTime != null && !"".equals(endTime)){
				endTime = com.cxdai.utils.DateUtils.dayOffset(DateUtils.parse(endTime, DateUtils.YMD_DASH),1).getTime()+"";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("userId", userId);
			
			
			
			int totalCount = drawLogRecordMapper
					.countDrawLogRecord(params);
			page.setTotalCount(totalCount);
			List<TurnDrawLogVO> list = drawLogRecordMapper.queryDrawLogRecordList(
					params, page);
			page.setResult(list);
			
			
			return page;
	}
	
	@Override
	public List<TurnDrawLogVO> queryDrawListByCnd(double userId,String startTime,String endTime) {
		
			if(startTime != null && !"".equals(startTime)){
				startTime = com.cxdai.utils.DateUtils.date2TimeStamp(startTime);
			}
			if(endTime != null && !"".equals(endTime)){
				endTime = com.cxdai.utils.DateUtils.dayOffset(DateUtils.parse(endTime, DateUtils.YMD_DASH),1).getTime()+"";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("userId", userId);
			List<TurnDrawLogVO> list = drawLogRecordMapper.queryOneMonthDrawLogRecordList(
					params);
			return list;
	}

}
