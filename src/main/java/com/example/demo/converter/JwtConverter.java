package com.example.demo.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtConverter implements Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate =
            MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    // todo: could also do this stuff in the Security Filter
    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);

        Map<String,Object> resourceAccessClaims = (Map<String, Object>) claims.get("resource_access");

        // todo: this should be a config property i.e. list of keycloak clients who are allow role access
        Map<String,Object> clientClaims = (Map<String, Object>) resourceAccessClaims.get("keycloak-angular");
        ArrayList<String> clientRoles = (ArrayList<String>) clientClaims.get("roles");

        convertedClaims.put("authorities", clientRoles);

        return convertedClaims;
    }
}
