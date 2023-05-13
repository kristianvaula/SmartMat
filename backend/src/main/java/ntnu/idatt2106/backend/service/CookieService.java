package ntnu.idatt2106.backend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service for cookie extraction.
 */
@Service
public class CookieService {

    /**
     * Extracts cookie from a HttpServletRequest
     * @param request HttpServletRequest
     * @return Cookie
     */
    public String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SmartMatAccessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
