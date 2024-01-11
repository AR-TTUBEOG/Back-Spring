package com.ttubeog.global.config.security.handler;

import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.OAuth2Config;
import com.ttubeog.global.config.security.util.CustomCookie;
import com.ttubeog.domain.auth.domain.Token;
import com.ttubeog.domain.auth.dto.TokenMapping;
import com.ttubeog.domain.auth.domain.repository.CustomAuthorizationRequestRepository;
import com.ttubeog.domain.auth.domain.repository.TokenRepository;
import com.ttubeog.domain.auth.application.CustomTokenProviderService;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

import static com.ttubeog.domain.auth.domain.repository.CustomAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class CustomSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    private final CustomTokenProviderService customTokenProviderService;
    private final OAuth2Config oAuth2Config;
    private final TokenRepository tokenRepository;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultAssert.isAuthentication(!response.isCommitted());

        String targetUrl = determineTargetUrl(request, response, authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        CustomCookie.addCookie(response, "Authorization", "Bearer_" + tokenMapping.getAccessToken(), (int) oAuth2Config.getAuth().getAccessTokenExpirationMsec());
        CustomCookie.addCookie(response, "Refresh_Token", "Bearer_" + tokenMapping.getRefreshToken(), (int) oAuth2Config.getAuth().getRefreshTokenExpirationMsec());

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CustomCookie.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        DefaultAssert.isAuthentication( !(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) );

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                .userEmail(tokenMapping.getUserEmail())
                .refreshToken(tokenMapping.getRefreshToken())
                .build();
        tokenRepository.save(token);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenMapping.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        customAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return oAuth2Config.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
