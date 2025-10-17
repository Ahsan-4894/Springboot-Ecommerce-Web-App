package com.zepox.EcommerceWebApp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class SetCookieInBrowser {

    public void setCookieInTheBrowser(HttpServletResponse response,
                                   String cookieName, String token, int TTL) {
//        Now set the cookie in the browser.
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(TTL); // 15 minutes
        response.addCookie(cookie);
    }
}
