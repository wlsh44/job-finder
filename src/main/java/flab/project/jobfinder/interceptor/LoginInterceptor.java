package flab.project.jobfinder.interceptor;

import flab.project.jobfinder.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        User user = (User) session.getAttribute(LOGIN_SESSION_ID);
        if (user != null) {
            log.info("user: {}", user.getName());
            request.setAttribute("user", user);
        }
    }
}
