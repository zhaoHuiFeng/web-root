package com.huifengzhao.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author huifengzhao
 * @ClassName BaseInterceptor
 * @date 2018/8/28
 */
public abstract class BaseInterceptor extends HandlerInterceptorAdapter {

    public static final transient Logger log = LoggerFactory.getLogger(AbstractBaseController.class);

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected ServletContext application;
    protected String url;
    private CallbackController callbackController;
    private static final ThreadLocal<HttpSession> HTTP_SESSION_THREAD_LOCAL = new ThreadLocal();

    public BaseInterceptor() {
    }

    public static HttpSession getSession() {
        return (HttpSession)HTTP_SESSION_THREAD_LOCAL.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.application = this.session.getServletContext();
        HTTP_SESSION_THREAD_LOCAL.set(this.session);
        this.url = request.getRequestURI();
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Object object = handlerMethod.getBean();
            Class controlClass = object.getClass();
            if (CallbackController.class.isAssignableFrom(controlClass)) {
                this.callbackController = (CallbackController)object;
            }
        }

        return true;
    }

    protected void reload() {
        if (this.callbackController != null) {
            this.callbackController.resetInfo();
        }

    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (response.getStatus() != HttpStatus.OK.value()) {
            log.info("请求错误:" + request.getRequestURI() + "  " + response.getStatus());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("controller 异常:" + request.getRequestURL(), ex);
        }

    }
}
