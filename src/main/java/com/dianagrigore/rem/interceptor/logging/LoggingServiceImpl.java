package com.dianagrigore.rem.interceptor.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoggingServiceImpl implements LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        Map<String, String> headersMap = buildHeadersMap(httpServletRequest);
        stringBuilder.append("REQUEST\n");
        stringBuilder.append("ID=[").append(headersMap.get("request-id")).append("] \n");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] \n");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] \n");
        stringBuilder.append("headers=[").append(headersMap).append("] \n");

        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] \n");
        }

        if (body != null) {
            stringBuilder.append("body=[").append(body).append("]\n");
        }

        logger.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {

        Map<String, String> headersMap = buildHeadersMap(httpServletRequest);
        String stringBuilder = "RESPONSE \n" +
                "ID=[" + headersMap.get("request-id") + "] \n" +
                "method=[" + httpServletRequest.getMethod() + "] \n" +
                "path=[" + httpServletRequest.getRequestURI() + "] \n" +
                "responseHeaders=[" + buildHeadersMap(httpServletResponse) + "] \n" +
                "responseBody=[" + body + "] \n";
        logger.info(stringBuilder);
    }

    @Override
    public void logError(HttpServletRequest httpServletRequest, Throwable throwable) {
        Map<String, String> headersMap = buildHeadersMap(httpServletRequest);
        logger.error("Exception caught for request-id=[{}]:", headersMap.get("request-id"), throwable);
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}
