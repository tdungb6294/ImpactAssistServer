package com.bdserver.impactassist.utils;

import org.springframework.stereotype.Service;

@Service
public class MyStringUtils {
    public static String camelCaseToSpacedWords(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(camelCaseString.charAt(0)));

        for (int i = 1; i < camelCaseString.length(); i++) {
            char currentChar = camelCaseString.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                result.append(' ');
                result.append(currentChar);
            } else {
                result.append(Character.toLowerCase(currentChar));
            }
        }

        return result.toString();
    }
}
