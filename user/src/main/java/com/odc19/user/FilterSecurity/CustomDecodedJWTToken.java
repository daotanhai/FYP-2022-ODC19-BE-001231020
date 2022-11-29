package com.odc19.user.FilterSecurity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CustomDecodedJWTToken {
    public Long decodedUserIdFromJWT(String jwtToken) {
        DecodedJWT decodedJWT = decodeJwt(jwtToken);
        return decodedJWT.getClaim("userId").asLong();
    }

    public HashMap<String, Long> getUserIdAndDepartmentIdFromJWT(String jwtToken) {
        HashMap<String, Long> hashMap = new HashMap<>();
        DecodedJWT decodedJWT = decodeJwt(jwtToken);
        hashMap.put("userId", decodedJWT.getClaim("userId").asLong());
        return hashMap;
    }

    private DecodedJWT decodeJwt(String jwtToken) {
        String token = jwtToken.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
