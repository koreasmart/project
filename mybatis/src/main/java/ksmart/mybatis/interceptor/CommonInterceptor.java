package ksmart.mybatis.interceptor;

import java.util.Set;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.mapper.MemberMapper;

@Component
public class CommonInterceptor implements HandlerInterceptor{
	
	private static final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private final MemberMapper memberMapper;
	
	public CommonInterceptor(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	/**
	 * HandlerMapping이 핸들러 객체를 결정한 후 HandlerAdapter가 핸들러를 호출하기 전에 호출되는 메소드
	 * return true(핸들러메소드 실행), false(핸들러 메소드 실행x: 핸들러까지 진입 금지)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("SID");
		
		Cookie cookie = WebUtils.getCookie(request, "loginKeepId");
		
		if(cookie != null) {
			String loginId = cookie.getValue();
			if(sessionId == null) {
				Member memberInfo = memberMapper.getMemberInfoById(loginId);
				if(memberInfo != null) {
					String memberLevel = memberInfo.getMemberLevel();
					String memberName = memberInfo.getMemberName();
					session.setAttribute("SID", loginId);
					session.setAttribute("SLEVEL", memberLevel);
					session.setAttribute("SNAME", memberName);
				}
			}
		}
		// 파라미터의 값을 확인 
		Set<String> paramKeySet = request.getParameterMap().keySet();
		
		StringJoiner param = new StringJoiner(", ");
		
		// ex:) memberId: id001, memberPw: pw001
		for(String key : paramKeySet) {
			param.add(key + ": " + request.getParameter(key));
		}
		
		log.info("ACCESS INFO ============================================");
		log.info("PORT 		::::::::	{}", request.getLocalPort());
		log.info("ServerName 		::::::::	{}", request.getServerName());
		log.info("HTTPMethod 		::::::::	{}", request.getMethod());
		log.info("URI 		::::::::	{}", request.getRequestURI());
		log.info("CLIENT IP 		::::::::	{}", request.getRemoteAddr());
		log.info("Parameter 		::::::::	{}", param);
		log.info("ACCESS INFO ============================================");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	/**
	 * HandlerAdapter가 실제로 핸들러를 호출한 후 DispatcherServlet이 뷰를 전달되기 전에 호출되는 메소드 
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	/**
	 * HandlerAdapter가 실제로 핸들러를 호출한 후 DispatcherServlet이 뷰를 랜더링한 후에 호출되는 메소드 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
