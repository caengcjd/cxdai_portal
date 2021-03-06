package com.cxdai.portal.chart.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cxdai.common.page.Page;
import com.cxdai.common.statics.Constants;
import com.cxdai.portal.chart.service.FinanceChartService;
import com.cxdai.portal.chart.vo.PublicChartsInfoVo;
import com.cxdai.portal.chart.vo.RepaymentChartCnd;
import com.cxdai.portal.chart.vo.RepaymentChartVo;
import com.cxdai.utils.DateUtils;

@Controller
@RequestMapping(value = "/chart/finance")
public class FinanceChartController {

	@Autowired
	private FinanceChartService financeChartService;

	/**
	 * 
	 * <p>
	 * Description:进入实时财务<br />
	 * </p>
	 * 
	 * @author yangshijin
	 * @version 0.1 2014年6月17日
	 * @param request
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/show")
	public ModelAndView show(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("/chart/realtime");
		return mav;
	}

	/**
	 * <p>
	 * Description:金额人数折线图<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2013年12月25日
	 * @param request
	 * @param username
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "moneyPersonChart")
	@ResponseBody
	public Map<String, Object> moneyPersonChart(HttpServletRequest request, String username) throws Exception {
		Map<String, Object> resultMap = financeChartService.queryMoneyPersonChart();
		return resultMap;
	}

	/**
	 * <p>
	 * Description:成交分布图<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2013年12月25日
	 * @param request
	 * @param username
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "successBorrowChart")
	@ResponseBody
	public PublicChartsInfoVo successBorrowChart(HttpServletRequest request, String username) throws Exception {
		PublicChartsInfoVo publicChartsInfo = financeChartService.querySuccessBorrowChart();
		if (request.getParameter("chartWidth") != null && !request.getParameter("chartWidth").equals("")) {
			publicChartsInfo.setChartWidth(Integer.parseInt(request.getParameter("chartWidth")));
		}
		if (request.getParameter("chartHight") != null && !request.getParameter("chartHight").equals("")) {
			publicChartsInfo.setChartHight(Integer.parseInt(request.getParameter("chartHight")));
		}
		return publicChartsInfo;
	}

	/**
	 * <p>
	 * Description:待收分布图<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2013年12月25日
	 * @param request
	 * @param username
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "waitReceiveChart")
	@ResponseBody
	public PublicChartsInfoVo waitReceiveChart(HttpServletRequest request, String username) throws Exception {
		PublicChartsInfoVo publicChartsInfo = financeChartService.queryWaitReceiveChart();
		if (request.getParameter("chartWidth") != null && !request.getParameter("chartWidth").equals("")) {
			publicChartsInfo.setChartWidth(Integer.parseInt(request.getParameter("chartWidth")));
		}
		if (request.getParameter("chartHight") != null && !request.getParameter("chartHight").equals("")) {
			publicChartsInfo.setChartHight(Integer.parseInt(request.getParameter("chartHight")));
		}
		return publicChartsInfo;
	}

	/**
	 * <p>
	 * Description:查询金额人数数据<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2013年12月25日
	 * @param request
	 * @param username
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "moneyPersonData")
	public ModelAndView moneyPersonData(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("chart/finance/moneyperson");
		Map<String, Object> resultMap = financeChartService.queryMoneyPersonData();
		mav.addObject("resultMap", resultMap);
		return mav;
	}

	/**
	 * 
	 * <p>
	 * Description:本周数据详情<br />
	 * </p>
	 * 
	 * @author yangshijin
	 * @version 0.1 2014年6月16日
	 * @param request
	 * @param pageSize
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/weekInfo/{pageSize}")
	public ModelAndView weekInfo(HttpServletRequest request, @PathVariable int pageSize) throws Exception {
		ModelAndView mv = new ModelAndView("/chart/week-info");
		String search_type = request.getParameter("search_type");
		Map<String, String> map = queryTimeByNow(search_type);

		String pageNum_str = request.getParameter("pageNum");
		int pageNum = 1;
		if (pageNum_str != null && !pageNum_str.equals("")) {
			pageNum = Integer.parseInt(pageNum_str);
		}
		RepaymentChartCnd repaymentChartCnd = new RepaymentChartCnd();
		if (map.get("beignTime") != null && !map.get("beignTime").equals("") && !map.get("beignTime").equals("null")) {
			repaymentChartCnd.setBeignTime(map.get("beignTime"));
		}
		if (map.get("endTime") != null && !map.get("endTime").equals("") && !map.get("endTime").equals("null")) {
			repaymentChartCnd.setEndTime(map.get("endTime"));
		}
		Page page = financeChartService.findRepaymentChartForWeekPages(repaymentChartCnd, pageNum, pageSize);
		request.setAttribute("repaymentChartVoMap", financeChartService.queryRepaymentChartForWeek(repaymentChartCnd));
		request.setAttribute("page", page);
		request.setAttribute("search_type", search_type);
		return mv;
	}

	/**
	 * 
	 * <p>
	 * Description:逾期列表详情<br />
	 * </p>
	 * 
	 * @author yangshijin
	 * @version 0.1 2014年6月16日
	 * @param request
	 * @param pageSize
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/overdueInfo/{pageSize}")
	public ModelAndView overdueInfo(HttpServletRequest request, @PathVariable int pageSize) throws Exception {
		ModelAndView mv = new ModelAndView("/chart/overdue-info");
		String pageNum_str = request.getParameter("pageNum");
		int pageNum = 1;
		if (pageNum_str != null && !pageNum_str.equals("")) {
			pageNum = Integer.parseInt(pageNum_str);
		}
		RepaymentChartCnd repaymentChartCnd = new RepaymentChartCnd();
		Page page = financeChartService.findRepaymentChartForOverduePages(repaymentChartCnd, pageNum, pageSize);
		request.setAttribute("page", page);
		return mv;
	}

	/**
	 * 
	 * <p>
	 * Description:结清列表详情<br />
	 * </p>
	 * 
	 * @author yangshijin
	 * @version 0.1 2014年6月16日
	 * @param request
	 * @param pageSize
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/finishInfo/{pageSize}")
	public ModelAndView finishInfo(HttpServletRequest request, @PathVariable int pageSize) throws Exception {
		ModelAndView mv = new ModelAndView("/chart/finish-info");
		String pageNum_str = request.getParameter("pageNum");
		int pageNum = 1;
		if (pageNum_str != null && !pageNum_str.equals("")) {
			pageNum = Integer.parseInt(pageNum_str);
		}
		RepaymentChartCnd repaymentChartCnd = new RepaymentChartCnd();
		Page page = financeChartService.findRepaymentChartForFinishPages(repaymentChartCnd, pageNum, pageSize);
		request.setAttribute("page", page);
		return mv;
	}

	/**
	 * 
	 * <p>
	 * Description:根据查询匹配类型获取相应的时间段<br />
	 * </p>
	 * 
	 * @author yangshijin
	 * @version 0.1 2014年6月16日
	 * @param search_type
	 * @return Map<String,String>
	 */
	public static Map<String, String> queryTimeByNow(String search_type) {
		Map<String, String> map = new HashMap<String, String>();
		Date now = new Date();
		String beignTime = "";
		String endTime = "";
		if (search_type != null) {
			if (search_type.equals("week")) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);
				// 当前日期是星期几
				int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
				if (w == 0) {
					beignTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.dayOffset(now, -6), DateUtils.YMD_DASH) + " 00:00:00");
					endTime = DateUtils.dateTime2TimeStamp(DateUtils.format(now, DateUtils.YMD_DASH) + " 23:59:59");
				} else {
					beignTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.dayOffset(now, 1 - w), DateUtils.YMD_DASH) + " 00:00:00");
					endTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.dayOffset(now, 7 - w), DateUtils.YMD_DASH) + " 23:59:59");
				}
				map.put("beignTime", beignTime);
				map.put("endTime", endTime);
			} else if (search_type.equals("month")) {
				beignTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.firstDay(now), DateUtils.YMD_DASH) + " 00:00:00");
				endTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.lastDay(now), DateUtils.YMD_DASH) + " 23:59:59");
				map.put("beignTime", beignTime);
				map.put("endTime", endTime);
			} else if (search_type.equals("quarter")) {
				// 当前日期是哪个月
				int now_month = DateUtils.getMonthByDay();
				if (now_month >= 1 && now_month <= 3) {
					beignTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-01-01 00:00:00");
					endTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-03-31 23:59:59");
				} else if (now_month >= 4 && now_month <= 6) {
					beignTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-04-01 00:00:00");
					endTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-06-30 23:59:59");
				} else if (now_month >= 7 && now_month <= 9) {
					beignTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-07-01 00:00:00");
					endTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-09-30 23:59:59");
				} else if (now_month >= 10 && now_month <= 12) {
					beignTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-10-01 00:00:00");
					endTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-12-31 23:59:59");
				}
				map.put("beignTime", beignTime);
				map.put("endTime", endTime);
			} else if (search_type.equals("year")) {
				beignTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-01-01 00:00:00");
				endTime = DateUtils.dateTime2TimeStamp(String.valueOf(DateUtils.getYearByDay()) + "-12-31 23:59:59");
				map.put("beignTime", beignTime);
				map.put("endTime", endTime);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			// 当前日期是星期几
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w == 0) {
				beignTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.dayOffset(now, -6), DateUtils.YMD_DASH) + " 00:00:00");
				endTime = DateUtils.dateTime2TimeStamp(DateUtils.format(now, DateUtils.YMD_DASH) + " 23:59:59");
			} else {
				beignTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.dayOffset(now, 1 - w), DateUtils.YMD_DASH) + " 00:00:00");
				endTime = DateUtils.dateTime2TimeStamp(DateUtils.format(DateUtils.dayOffset(now, 7 - w), DateUtils.YMD_DASH) + " 23:59:59");
			}
			map.put("beignTime", beignTime);
			map.put("endTime", endTime);
		}
		return map;
	}

	public List<RepaymentChartVo> queryRepaymentChartVoList(List<RepaymentChartVo> list) {
		List<RepaymentChartVo> listReturn = new ArrayList<RepaymentChartVo>();
		for (int i = 0; i < list.size(); i++) {
			RepaymentChartVo repaymentChartVo = list.get(i);
			Date repayday = DateUtils.parse(DateUtils.timeStampToDate(repaymentChartVo.getRepaymentTime(), DateUtils.YMD_DASH), DateUtils.YMD_DASH);
			int day = DateUtils.dayDiff(new Date(), repayday);
			if (day > 0) {
				if (repaymentChartVo.getBorrowStatus() == 4 || repaymentChartVo.getBorrowStatus() == 42) {
					BigDecimal account = repaymentChartVo.getRepaymentAccount();
					BigDecimal lateDayInterest = account.multiply(new BigDecimal(Constants.OUT_OF_DAYE_RATE)).multiply(new BigDecimal(day));
					if ((repaymentChartVo.getBorrowStatus() == 42 || repaymentChartVo.getBorrowStatus() == 4) && (repaymentChartVo.getStatus() == 0)) {
						repaymentChartVo.setLateDays(day);
						repaymentChartVo.setLateInterest(lateDayInterest);
					}
				}
			} else {
				repaymentChartVo.setLateDays(0);
				repaymentChartVo.setLateInterest(new BigDecimal(0));
			}
			if (repaymentChartVo.getFirstAccount() != null) {
				repaymentChartVo.setFirstAccount(repaymentChartVo.getFirstAccount().setScale(2, BigDecimal.ROUND_DOWN));
			}
			repaymentChartVo.setRepaymentAccount(repaymentChartVo.getRepaymentAccount().setScale(2, BigDecimal.ROUND_DOWN));
			repaymentChartVo.setLateInterest(repaymentChartVo.getLateInterest().setScale(2, BigDecimal.ROUND_DOWN));
			listReturn.add(repaymentChartVo);
		}
		return listReturn;
	}
}
