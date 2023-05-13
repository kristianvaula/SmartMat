package ntnu.idatt2106.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CookieServiceTest {

    @Mock
    private HttpServletRequest request;

    private CookieService cookieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cookieService = new CookieService();
    }

    @Test
    void testExtractTokenFromCookie() {

        Cookie cookie = new Cookie("SmartMatAccessToken", "token123");
        when(request.getCookies()).thenReturn(new Cookie[] {cookie});


        String token = cookieService.extractTokenFromCookie(request);


        assertEquals("token123", token);
    }

    @Test
    void testExtractTokenFromCookieNoCookieFound() {

        when(request.getCookies()).thenReturn(null);


        String token = cookieService.extractTokenFromCookie(request);


        assertNull(token);
    }

    @Test
    void testExtractTokenFromCookieOtherCookieFound() {

        Cookie cookie = new Cookie("OtherCookie", "value123");
        when(request.getCookies()).thenReturn(new Cookie[] {cookie});


        String token = cookieService.extractTokenFromCookie(request);


        assertNull(token);
    }
}
