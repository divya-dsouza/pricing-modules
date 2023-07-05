package org.divyad.filter;

import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

import static java.util.Objects.nonNull;

@Component
@Slf4j
public class AuthorizationFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authorizationHeader = request.getHeaders().get("Authorization").get(0);
            String userName = null;

            String idToken = authorizationHeader.replaceFirst("Bearer ", "");
            if (nonNull(idToken) && !idToken.isEmpty()) {
                if (isTokenExpired(idToken)) {
                    final var errorMessage = "Invalid JWT. Either expired or not yet valid. ";
                    log.warn(errorMessage);
                } else {

                    try {
                        userName = (String) JWTParser.parse(idToken)
                                .getJWTClaimsSet().getClaim("email");
                        Date iat = (Date) JWTParser.parse(idToken).getJWTClaimsSet().getClaim("iat");
                        log.info("Got issued at:" + iat);

                    } catch (ParseException e) {
                        log.error(String.format("Exception occurred while parsing token : %s", idToken), e);
                    }
                    log.info("user logged in {}", userName);
                }
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest();
            modifiedRequest = modifiedRequest.mutate().header("USERNAME", userName)
                    .build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        }, 1);
    }


    private boolean isTokenExpired(String idToken) {
        Date expDate = null;
        try {
            expDate = (Date) JWTParser.parse(idToken).getJWTClaimsSet().getClaim("exp");
            if (expDate != null) {
                return expDate.before(new Date());
            }
        } catch (java.text.ParseException e) {
            log.error("error while parsing token for expiry date", e);
        }
        return true;
    }
}