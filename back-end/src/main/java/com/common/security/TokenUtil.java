package com.common.security;

import com.auth0.jwt.interfaces.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/13.
 */
public class TokenUtil {
    private final static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public static HttpServletRequest getRequest(){
        try{
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * @Title: getUserId @Description:获取token中的用户id @param
     * httpServletRequest @param httpServletResponse @return @throws
     */
    public static Integer getUserId() {
        HttpServletRequest httpServletRequest = TokenUtil.getRequest();
        String token = httpServletRequest.getHeader("x-access-token");
        String userid = null;
        try {
            Map<String, Claim> claims = JwtUtil.verifyToken(token);
            userid = claims.get("user_id").toString().split("::")[1];
        } catch (Exception e) {
//			e.printStackTrace();
            userid = "0";
        }
        return Integer.parseInt(userid);
    }

    public static String getUserName(){
        HttpServletRequest httpServletRequest = TokenUtil.getRequest();
        String token = httpServletRequest.getHeader("x-access-token");
        String username = null;
        try {
            Map<String, Claim> claims = JwtUtil.verifyToken(token);
            username = claims.get("user_id").toString().split("::")[0];
        } catch (Exception e) {
//            e.printStackTrace();
            username = "testname";
        }
        return username;
    }
}
