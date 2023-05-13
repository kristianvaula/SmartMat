package ntnu.idatt2106.backend.config;


import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.enums.AuthenticationState;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.JwtService;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWTAuthentication filter is used for authenticating users based on JWT token
 * The JWTAuthentication filter validates the token before the request is sent to a controller
 */
@Component
@RequiredArgsConstructor
public class JwTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CookieService cookieService;

    /**
     * Method for extracting the jwt token from the cookie.
     * The method validates the extracted cookie and sets the authentication state of the request.
     *
     * @param request The HttpServletRequest object for this request.
     * @param response The HttpServletResponse object for this request.
     * @param filterChain FilterChain object for this request.
     * @throws ServletException if the request could not be handled
     * @throws IOException if the response could not be written
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Bypass filter for login and register endpoints
        if (requestURI != null &&
                (requestURI.startsWith("/h2-ui") // change this line
                        || requestURI.startsWith("/swagger-ui")
                        || requestURI.startsWith("/v3/api-docs")
                        || requestURI.matches("/api/auth/(login|register)")
                        || requestURI.matches(".*(css|jpg|png|gif|js|html|ico)$"))) {
            filterChain.doFilter(request, response);
            return;
        }


        final String jwt = cookieService.extractTokenFromCookie(request);

        String username = null;
        AuthenticationState authState = AuthenticationState.UNAUTHENTICATED;

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (jwt != null) {
            try {
                username = jwtService.extractUsername(jwt);
                authorities = jwtService.extractRoles(jwt).stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            } catch (ExpiredJwtException e) {
                authState = AuthenticationState.TOKEN_EXPIRED;
                username = null;
            } catch (Exception e) {
                username = null;
            }
        }

        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                authState = AuthenticationState.AUTHENTICATED;
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        request.setAttribute("authState", authState);

        filterChain.doFilter(request, response);
    }

}
