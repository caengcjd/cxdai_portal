package com.cxdai.portal.account.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cxdai.common.page.Page;
import com.cxdai.common.statics.Constants;
import com.cxdai.portal.account.service.AccountService;
import com.cxdai.portal.account.service.BorrowReportService;
import com.cxdai.portal.account.service.CashRecordService;
import com.cxdai.portal.account.vo.AccountVo;
import com.cxdai.portal.account.vo.CashRecordCnd;
import com.cxdai.portal.account.vo.TakeCashMoneyVo;
import com.cxdai.portal.base.BaseController;
import com.cxdai.portal.base.MessageBox;
import com.cxdai.portal.member.service.BankInfoService;
import com.cxdai.portal.member.service.MemberService;
import com.cxdai.portal.member.service.RealNameApproService;
import com.cxdai.portal.member.service.VipLevelService;
import com.cxdai.portal.member.vo.BankInfoVo;
import com.cxdai.portal.member.vo.MemberApproVo;
import com.cxdai.portal.member.vo.MemberCnd;
import com.cxdai.portal.member.vo.MemberVo;
import com.cxdai.portal.statics.BusinessConstants;
import com.cxdai.security.ShiroUser;
import com.cxdai.utils.exception.AppException;


/**
 * <p>
 * Description:提现记录控制类<br />
 * </p>
 * 
 * @title CashRecordController.java
 * @package com.cxdai.portal.account.controller
 * @author justin.xu
 * @version 0.1 2014年6月18日
 */
@Controller
@RequestMapping(value = "/myaccount/cashRecord")
public class CashRecordController extends BaseController {

	private final static Logger logger = Logger.getLogger(CashRecordController.class);
	@Autowired
	private CashRecordService cashRecordService;
	@Autowired
	private BankInfoService bankInfoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private RealNameApproService realNameApproService;
	@Autowired
	private BorrowReportService borrowReportService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private VipLevelService vipLevelService;

	/**
	 * <p>
	 * Description:跳转到提现菜单页面<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年5月21日
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "toCashIndex")
	@RequiresAuthentication
	public ModelAndView toTopupMain() throws Exception {
		ModelAndView mv = new ModelAndView("account/cash/cashIndex");
		ShiroUser shiroUser = currentUser();
		if (super.judgeBlackByType(BusinessConstants.BLACK_TYPE_CASH)) {
			mv = new ModelAndView("redirect:/myaccount/toIndex.html");
			return mv;
		}
		MemberCnd memberCnd = new MemberCnd();
		memberCnd.setId(shiroUser.getUserId());
		MemberVo memberVo = memberService.queryMemberByCnd(memberCnd);

		// 查询用户认证信息
		MemberApproVo memberApproVo = memberService.queryMemberApproByUserId(shiroUser.getUserId());
		// 您还没有进行手机认证，请先进行手机认证
		if (null == memberApproVo.getMobilePassed() || memberApproVo.getMobilePassed() != Constants.YES) {
			mv.addObject("errorCode", "-3");
			return mv;
		}
		// 判断是否通过了实名认证
		if (null == memberApproVo.getNamePassed() || memberApproVo.getNamePassed() != Constants.REALNAME_APPR_ISPASSED_PASSED) {
			mv.addObject("errorCode", "-1");
			return mv;
		}

		// 如果交易密码为空，则跳到交易密码页面
		if (null == memberVo.getPaypassword() || "".equals(memberVo.getPaypassword())) {
			mv.addObject("errorCode", "-4");
			return mv;
		}

		// 设置菜单名
		mv.addObject(BusinessConstants.ACCOUNT_FIRST_MENU, BusinessConstants.LEFT_MENU_ZJ);
		mv.addObject(BusinessConstants.ACCOUNT_SECOND_MENU, BusinessConstants.LEFT_MENU_ZJ_TAKE_CASH);
		return mv;
	}

	/**
	 * <p>
	 * Description:进入提现页面<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年6月18日
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 *             String
	 */
	@RequestMapping(value = "toGetcash")
	@RequiresAuthentication
	public ModelAndView getcash(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView("account/cash/getcash");
		ShiroUser shiroUser = currentUser();
		if (super.judgeBlackByType(BusinessConstants.BLACK_TYPE_CASH)) {
			mav = new ModelAndView("redirect:/myaccount/toIndex.html");
			return mav;
		}
		// 当前帐号信息
		AccountVo accountVo = accountService.queryAccountByUserId(shiroUser.getUserId());
		mav.addObject("accountVo", accountVo);

		BigDecimal maxDrawMoney = cashRecordService.getMaxDrawMoney(shiroUser.getUserId());

		// 当前用户银行卡信息
		BankInfoVo currentBankCardVo = bankInfoService.getUserCurrentCard(shiroUser.getUserId());

		// 查询银行卡数量；
		int cardNum = bankInfoService.querytBankCardNum(shiroUser.getUserId());
		mav.addObject("cardNum", cardNum);

		// 查询银行卡操作日志中的锁定记录(type=0的记录)
		int cardLock = bankInfoService.querytBankCardLock(shiroUser.getUserId());
		mav.addObject("cardLock", cardLock);

		// 查询用户信息
		MemberCnd memberCnd = new MemberCnd();
		memberCnd.setId(shiroUser.getUserId());
		MemberVo memberVo = memberService.queryMemberByCnd(memberCnd);
		// 未设置交易密码
		if (null == memberVo.getPaypassword() || "".equals(memberVo.getPaypassword())) {
			mav.addObject("nosetPaypassword", true);
		}
		if (vipLevelService.getIsSvipByUserId(shiroUser.getUserId())) {
			mav.addObject("isSvip", "yes");
		} else {
			mav.addObject("isSvip", "no");
		}
		mav.addObject("currentBankCardVo", currentBankCardVo);
		mav.addObject("maxDrawMoney", maxDrawMoney);
		Integer getCashedCount = cashRecordService.getCashedCount(shiroUser.getUserId(), new Date());
		mav.addObject("getCashedCount", getCashedCount);
		return mav;
	}

	/**
	 * <p>
	 * Description:查询当前用户的提现记录.<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年6月18日
	 * @param request
	 * @param pageNo
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value = "/queryPageList/{pageNo}")
	@RequiresAuthentication
	public ModelAndView queryPageList(HttpServletRequest request, @PathVariable Integer pageNo) throws Exception {
		ModelAndView mv = new ModelAndView("account/cash/cashRecordList");
		ShiroUser shiroUser = currentUser();
		CashRecordCnd cashRecordCnd = new CashRecordCnd();
		cashRecordCnd.setUserId(shiroUser.getUserId());
		Page page = cashRecordService.queryPageListByCnd(cashRecordCnd, new Page(pageNo, BusinessConstants.DEFAULT_PAGE_SIZE));
		mv.addObject("page", page);
		return mv;
	}

	/**
	 * <p>
	 * Description:取消提现<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年6月18日
	 * @param request
	 * @param session
	 * @param response
	 * @param shengReceiveForm
	 * @return String
	 */
	@RequestMapping(value = "cancelCash")
	@RequiresAuthentication
	public @ResponseBody
	MessageBox cancelCash(HttpServletRequest request, HttpSession session, HttpServletResponse response, CashRecordCnd cashRecordCnd) {
		String result = BusinessConstants.SUCCESS;
		try {
			ShiroUser shiroUser = currentUser();
			cashRecordCnd.setUserId(shiroUser.getUserId());
			result = cashRecordService.saveCancelCash(cashRecordCnd, request);
			if (!"success".equals(result)) {
				return new MessageBox("0", result);
			}
		} catch (AppException ae) {
			return new MessageBox("0", ae.getMessage());
		} catch (Exception e) {
			logger.error("取消提现出错", e);
			return new MessageBox("0", "网络连接异常，请刷新页面或稍后重试!");
		}
		return new MessageBox("1", result);
	}

	/**
	 * <p>
	 * Description:保存提现申请<br />
	 * </p>
	 * 
	 * @author justin.xu
	 * @version 0.1 2014年6月19日
	 * @param request
	 * @param session
	 * @param response
	 * @param takeCashMoneyVo
	 * @return String
	 */
	@RequestMapping(value = "saveTakeCash")
	@RequiresAuthentication
	public @ResponseBody
	String saveTakeCash(HttpServletRequest request, HttpSession session, HttpServletResponse response, TakeCashMoneyVo takeCashMoneyVo) {
		String result = "success";
		try {
			ShiroUser shiroUser = currentUser();
			if (super.judgeBlackByType(BusinessConstants.BLACK_TYPE_CASH)) {
				return "";
			}
			if (shiroUser.getIsFinancialUser() == 0) {
				return "借款用户无法提现！";
			}
			// 查询用户认证信息
			MemberApproVo memberApproVo = memberService.queryMemberApproByUserId(shiroUser.getUserId());
			// 您还没有进行手机认证，请先进行手机认证
			if (null == memberApproVo.getMobilePassed() || memberApproVo.getMobilePassed() != Constants.YES) {
				return "请先进行手机认证";
			}
			// 判断是否通过了实名认证
			if (null == memberApproVo.getNamePassed() || memberApproVo.getNamePassed() != Constants.REALNAME_APPR_ISPASSED_PASSED) {
				return "请先进行实名认证";
			}
			MemberCnd memberCnd = new MemberCnd();
			memberCnd.setId(shiroUser.getUserId());
			MemberVo memberVo = memberService.queryMemberByCnd(memberCnd);
			// 未设置交易密码
			if (null == memberVo.getPaypassword() || "".equals(memberVo.getPaypassword())) {
				return "请先设置交易密码";
			}
			if(null!=takeCashMoneyVo&&StringUtils.isNotEmpty(takeCashMoneyVo.getTakeMoney())){
				if(Double.valueOf(takeCashMoneyVo.getTakeMoney())<100){
					return "提现金额必须大于￥100！";
				}
				if (Double.valueOf(takeCashMoneyVo.getTakeMoney())> 500000) {
					return "提现金额必须小于￥500000！";
				}
			}else{
				return "提现金额不能为空！";
			}
			result = cashRecordService.saveTakeCash(takeCashMoneyVo, memberVo, request);
			if (!"success".equals(result)) {
				return result;
			}
		} catch (AppException ae) {
			return ae.getMessage();
		} catch (Exception e) {
			logger.error("保持提现出错", e);
			return "网络连接异常，请刷新页面或稍后重试!";
		}
		return result;
	}
}
