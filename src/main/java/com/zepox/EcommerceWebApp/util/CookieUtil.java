package com.zepox.EcommerceWebApp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void setCookieInTheBrowser(HttpServletResponse response,
                                   String cookieName, String token, int TTL) {
        try{
//            Now set the cookie in the browser.
            Cookie cookie = new Cookie(cookieName, token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(TTL); // 15 minutes
            response.addCookie(cookie);
        }catch(Exception ex){
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void deleteCookieFromBrowser(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(token, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete immediately
        response.addCookie(cookie);
    }
}
