package com.pos.medicineApp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.medicineApp.exceptions.BadGatewayException;
import com.pos.medicineApp.exceptions.UnauthorizedException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Utils {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static String getDateAsStringFromCNP(String cnp) {
        char firstNumber = cnp.charAt(0);
        String year = cnp.substring(1, 3);
        String month = cnp.substring(3, 5);
        String day = cnp.substring(5, 7);

        if (firstNumber == '1' || firstNumber == '2') {
            year = "19" + year;
        } else {
            year = "20" + year;
        }
        return String.format("%s-%s-%s", year, month, day);
    }

    public static Date calculateBirthDateFromCNP(String cnp) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(getDateAsStringFromCNP(cnp));
    }

    /**
     * Extracts the token from the given headers map. The token is retrieved from the "Authorization" header.
     *
     * @param  headers  a map containing the request headers
     * @return          the extracted token without the "Bearer " prefix
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
     * @param  json  the JSON string to parse
     * @return       an IDMUserInfo object representing the parsed data
     * @throws BadGatewayException if the JSON string is invalid
     */
    public static IDMUserInfo getUserInfo(String json) {
        try {
            return objectMapper.readValue(json, IDMUserInfo.class);
        } catch (Exception e) {
            throw new BadGatewayException("Invalid user info.");
        }
    }
}

