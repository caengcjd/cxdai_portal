package com.cxdai.portal.base;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import com.cxdai.common.ClientSavedRequest;

import com.cxdai.common.aop.exception.AuthenticationException;

public class MyExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		logger.error(exception.getClass(), exception);
		
		if (handlerMethod == null) {
			return null;
		}

		Method method = handlerMethod.getMethod();
		if (method == null) {
			return null;
		}
		
		// 该异常为重复提交，跳转到友好页面
		if (exception instanceof AuthenticationException) {
			return new ModelAndView("/common/duplicatesubmission");
		}
				
		boolean isAjaxResponse = (AnnotationUtils.findAnnotation(method, ResponseBody.class) != null);		
		if (exception instanceof ShiroException) {
			boolean isLogin = SecurityUtils.getSubject().isAuthenticated();
			if (isLogin) {
				if (isAjaxResponse) {
					return redirect("/message.html?code=0&message=Unoperable");
				} else {
					return redirect("/error/403.html");
				}
			} else {
				if (isAjaxResponse) {
					return redirect("/message.html?code=0&message=Unoperable");
				} else {
					if(exception instanceof UnauthenticatedException){
				        SavedRequest savedRequest = new ClientSavedRequest(request,null);
				        return redirect("/member/toLogin.html","backUrl",savedRequest.getRequestUrl());
					}
					return redirect("/member/toLogin.html");
				}
			}
		}
		if (isAjaxResponse) {
			return redirect("/message?code=0&message=Error");
		} else {
			return redirect("/error/500.html");
		}
	}
	
	private ModelAndView redirect(String viewName) {
		return new ModelAndView("redirect:" + viewName);
	}
	
	private ModelAndView redirect(String viewName,String name,String value) {
		return new ModelAndView("redirect:" + viewName,name,value);
	}
	
	public String encodeURL(String input) {
		if (input == null) {
			return null;
		}
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("encode failed", e);
		}
		return input;
	}
}
