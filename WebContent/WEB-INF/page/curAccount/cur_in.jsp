<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/page/common/public.jsp"%>
<title>转出_活期宝_我的账号</title>
</head>
<body style="overflow-y:hidden;background:#fff;">
<form action="" method="post" name="curInForm" id="curInForm">
<input type="hidden" name="useMoney" id="useMoney" value="${maxAccount}"/>
<input type="hidden" name="targetFrame" id="targetFrame" value="${targetFrame}"/>
<div class="sf">
	  <dl style="padding: 0px 0; margin-top:40px">
	      <dd><span>可转入金额 ：</span> <fmt:formatNumber value="${maxAccount}" pattern="#,##0.00" />元</dd>
	      <dd>
	      	<span><font color="red">*</font>金额 ：</span> <input type="text" name="tenderMoney" id="tenderMoney" style="width:150px"/>
	      		<a style="color:blue" href="javascript:enterMomey();">自动输入金额</a>
	      	</dd>
	      <dd>
	      	<span><font color="red">*</font>交易密码 ：</span> <input type="password" name="payPassword" id="payPassword" style="width:150px" />
	      	<a href="#" style="color:blue" onclick="goFindLoginPwd()">忘记密码?</a>
	      </dd>
	  </dl>
	  
	  <dl style="padding: 0px 0;">
		  	<dd>
			  	<div class="gg_btn">
				  	<table>
				  		<tr>
				  			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				  			<td><input id="save_curIn" type="button" value="提交" onclick="saveCurIn()" style="cursor:pointer;"/></td>
				  			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				  			<td><input type="button" value="取消" onclick="closeCurIn()" /></td>
				  		</tr>
				  	</table>
			  	</div>
		  	</dd>
	  </dl>
</div>
</form>
</body>
<script type="text/javascript">
var _load;
function validateData(){
	//金额的正则表达式
	var zfdsReg = /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;
	var reg= /^\d+[\.]?\d{0,2}$/g;
	/* if(new Number('${currentHourNum}') >= 0 && new Number('${currentHourNum}') <= 4){
		parent.layer.msg("耐心等待一会，系统在努力结算", 2, 5);
		return false;
	} */
	
	var account = $("#tenderMoney").val();
	if(account == null || account == ""){
		parent.parent.layer.msg("转入金额未填写!", 2, 5);
		return false;
	}
	if(!zfdsReg.test(account)){
		parent.layer.msg("转入金额输入有误!", 2, 5);
		return false;
	}
	if(!reg.test(account)){
		parent.layer.msg("转入金额只能保留2位小数!", 2, 5);
		return false;
	}
	if(account > new Number('${maxAccount}')){
		parent.layer.msg("账户余额小于加入金额，不能加入", 2, 5);
		return false;
	}
	if(account < 1){
		parent.layer.msg("转入金额必须大于1元!", 2, 5);
		return false;
	}
	var paypassword = $("#payPassword").val();
	if(paypassword == null || paypassword == ""){
		parent.layer.msg("交易密码未填写!", 2, 5);
		return false;
	}
	return true;
}

/**
 * 点击自动输入金额触发事件
 */
function enterMomey(){
	if(${null==portal:currentUser()}){
		 layer.msg("请先登录", 1, 5,function(){
			 parent.window.location.href="${path}/member/toLogin.html";
		 });
		 return;
	}
	if(${portal:hasRole("borrow")}){
		parent.layer.msg("您是借款用户,不能进行此操作", 1, 5);
		 return;
	}
	var useMoney = Number($("#useMoney").val());
	$("#tenderMoney").val(useMoney);
}

/**
 * 可用余额转入到活期宝
 */
function saveCurIn(){
	$("#save_curIn").removeAttr("onclick");
	var tenderMoney = $("#tenderMoney").val();
	if(validateData()){
		if(parent.layer.confirm("您确定要把"+tenderMoney+"元（"+numToChinese(tenderMoney)+"）"+"转入活期宝吗？",function(){
			_load = parent.layer.load('处理中..');
			//提交后台
			$("#curInForm").ajaxSubmit({
				url : '${basePath }/curInController/doMyCurrentIn.html',
				type : "POST",
				success : function(data) {
					layer.close(_load);
					$("#save_curIn").attr("onclick", "saveCurIn()");
					if(data.code=="1"){
						parent.layer.alert('此次交易成功转入'+$("#tenderMoney").val()+'元到活期宝', 1, "温馨提示", function(){
							if($("#targetFrame").val() == 1){
								window.open("${path}/curAccountController/curAccountInterest.html","_parent");
							}else{
								window.open("${path}/myaccount/toIndex.html","_parent");
							}
						});
					}else{
						if("0"==data.code || "-1"==data.code || "-2"==data.code || "-3"==data.code || "-4"==data.code || "-5"==data.code){
							parent.layer.msg(data.message, 1, 5,function(){
								if("0"==data.code){
									parent.window.location.href="${path}/member/toLogin.html";
								}else if("-1"==data.code){
									parent.window.location.href="${path}/AccountSafetyCentre/safetyIndex.html";
							    }else if("-2"==data.code){
							    	parent.window.location.href="${path}/account/approve/realname/toRealNameAppro.html";
							    }else if("-4"==data.code){
							    	parent.window.location.href="${path}/account/safe/toSetPayPwd.html";
							    }else if("-5"==data.code){
							    	parent.window.location.href="${path}/bankinfo/toBankCard.html";
							    }
								//当你在iframe页面关闭自身时
								var index = parent.layer.getFrameIndex(window.name);
								parent.layer.close(index);
							});
							return;
						}
						if (data.code == '99' || data.code == '-6') {
							parent.layer.close(_load);
							parent.layer.msg(data.message, 2, 5);
							loadimage();
						}
					}
				},
				error : function(data) {
					layer.close(_load);
					$("#save_curIn").attr("onclick", "saveCurIn()");
					parent.layer.msg("网络连接超时，请您稍后重试", 3, 5);
				}
			});
		}, function() {
			$("#save_curIn").attr("onclick", "saveCurIn()");
		}));
		 
	}else{
		$("#save_curIn").attr("onclick", "saveCurIn()");
	}	
}

/**
 * 点击取消按钮
 */
function closeCurIn(){
	if($("#targetFrame").val() == 1){
		parent.window.open("${path}/curAccountController/curAccountInterest.html","_self");
	}else{
		window.open("${path}/myaccount/toIndex.html","_self");
	}
	
	//当你在iframe页面关闭自身时
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

/**
 * 点击忘记密码
 */
function goFindLoginPwd(){
	parent.window.open("${path}/AccountSafetyCentre/safetyCentre/enterFindLoginPwd.html","_self");
	//当你在iframe页面关闭自身时
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

function numToChinese(Num){ 
	for(i=Num.length-1;i>=0;i--) { 
		Num = Num.replace(",","") 
		Num = Num.replace(" ","") 
	} 
	Num = Num.replace("￥","") 
	if(isNaN(Num)) { 
		layer.msg("金额格式不正确。", 2, 5);
		return; 
	} 
	part = String(Num).split("."); 
	newchar = ""; 
	for(i=part[0].length-1;i>=0;i--){ 
		tmpnewchar = "" 
		perchar = part[0].charAt(i); 
		switch(perchar){ 
		case "0": tmpnewchar="零" + tmpnewchar ;break; 
		case "1": tmpnewchar="壹" + tmpnewchar ;break; 
		case "2": tmpnewchar="贰" + tmpnewchar ;break; 
		case "3": tmpnewchar="叁" + tmpnewchar ;break; 
		case "4": tmpnewchar="肆" + tmpnewchar ;break; 
		case "5": tmpnewchar="伍" + tmpnewchar ;break; 
		case "6": tmpnewchar="陆" + tmpnewchar ;break; 
		case "7": tmpnewchar="柒" + tmpnewchar ;break; 
		case "8": tmpnewchar="捌" + tmpnewchar ;break; 
		case "9": tmpnewchar="玖" + tmpnewchar ;break; 
		} 
		switch(part[0].length-i-1){ 
		case 0: tmpnewchar = tmpnewchar +"元" ;break; 
		case 1: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break; 
		case 2: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break; 
		case 3: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break; 
		case 4: tmpnewchar= tmpnewchar +"万" ;break; 
		case 5: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break; 
		case 6: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break; 
		case 7: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break; 
		case 8: tmpnewchar= tmpnewchar +"亿" ;break; 
		case 9: tmpnewchar= tmpnewchar +"拾" ;break; 
	} 
		newchar = tmpnewchar + newchar; 
	} 
	if(Num.indexOf(".")!=-1){ 
		if(part[1].length > 2) { 
			part[1] = part[1].substr(0,2) 
		} 
		for(i=0;i<part[1].length;i++){ 
			tmpnewchar = "" 
			perchar = part[1].charAt(i) 
			switch(perchar){ 
			case "0": tmpnewchar="零" + tmpnewchar ;break; 
			case "1": tmpnewchar="壹" + tmpnewchar ;break; 
			case "2": tmpnewchar="贰" + tmpnewchar ;break; 
			case "3": tmpnewchar="叁" + tmpnewchar ;break; 
			case "4": tmpnewchar="肆" + tmpnewchar ;break; 
			case "5": tmpnewchar="伍" + tmpnewchar ;break; 
			case "6": tmpnewchar="陆" + tmpnewchar ;break; 
			case "7": tmpnewchar="柒" + tmpnewchar ;break; 
			case "8": tmpnewchar="捌" + tmpnewchar ;break; 
			case "9": tmpnewchar="玖" + tmpnewchar ;break; 
			} 
			if(i==0)tmpnewchar =tmpnewchar + "角"; 
			if(i==1)tmpnewchar = tmpnewchar + "分"; 
			newchar = newchar + tmpnewchar; 
		} 
	} 
	while(newchar.search("零零") != -1) 
	newchar = newchar.replace("零零", "零"); 
	newchar = newchar.replace("零亿", "亿"); 
	newchar = newchar.replace("亿万", "亿"); 
	newchar = newchar.replace("零万", "万"); 
	newchar = newchar.replace("零元", "元"); 
	newchar = newchar.replace("零角", ""); 
	newchar = newchar.replace("零分", ""); 
	if (newchar.charAt(newchar.length-1) == "元" || newchar.charAt(newchar.length-1) == "角") 
	newchar = newchar+"整" 
	return newchar; 
}

</script>
</html>
