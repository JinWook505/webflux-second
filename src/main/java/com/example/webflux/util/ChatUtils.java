package com.example.webflux.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatUtils {

    public static String extractJsonString(String content) {
        int startIdx = content.indexOf('{');
        int endIdx = content.lastIndexOf('}');

        if (startIdx != -1 &&
                endIdx != -1 &&
                startIdx < endIdx) {
            return content.substring(startIdx, endIdx + 1);
        }

        log.error("extractJsonString error. content : " + content);
        return "";
    }
}
