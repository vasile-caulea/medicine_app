package com.pos.medicineConsults.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.medicineConsults.exceptions.UnauthorizedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Utils {

    static ObjectMapper objectMapper = new ObjectMapper();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Extracts the token from the given headers map. The token is retrieved from the "Authorization" header.
     *
     * @param headers a map containing the request headers
     * @return the extracted token without the "Bearer " prefix
     * @throws UnauthorizedException if the token is invalid
     */
    public static String extractToken(Map<String, String> headers) {
        String token = headers.getOrDefault("authorization", "");
        if (!token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authorization header.");
        }
        return token.substring(7);
    }

    /**
     * Parses the given JSON string and returns an IDMUserInfo object.
     *
     * @param json the JSON string to parse
     * @return an IDMUserInfo object representing the parsed data
     */
    public static IDMUserInfo getUserInfo(String json) {
        try {
            return objectMapper.readValue(json, IDMUserInfo.class);
        } catch (Exception e) {
            // throw new BadGatewayException("Invalid user info.");
            return null;
        }
    }

    public static JsonNode getJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFormattedDate(Date date) {
        return sdf.format(date);
    }
}
