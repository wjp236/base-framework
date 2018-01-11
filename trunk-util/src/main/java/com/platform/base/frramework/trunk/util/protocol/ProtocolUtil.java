package com.platform.base.frramework.trunk.util.protocol;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by mylover on 4/22/16.
 */
public class ProtocolUtil {

    public static int doCheckAccept(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.indexOf("json") > -1?1:0;
    }

    public static int doCheckContentType(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.indexOf("json") > -1?1:0;
    }

    /**
     * print body,header
     * @param request
     * @param body
     * @return
     */
    public static String getHttpRequestPacket(HttpServletRequest request, String body) {
        StringBuilder sb = new StringBuilder("@Received HTTP Packet - \r\n\r\n");
        sb.append(request.getMethod() + " ");
        sb.append(request.getRequestURI()).append(request.getQueryString() == null?" ":"?" + request.getQueryString() + " ");
        sb.append(request.getProtocol() + "\r\n");
        Enumeration enumer = request.getHeaderNames();

        while(enumer.hasMoreElements()) {
            String name = (String)enumer.nextElement();
            String value = request.getHeader(name);
            sb.append(name + ": " + value + "\r\n");
        }

        if(body != null && body.length() > 0) {
            sb.append("\r\n").append(body);
        }

        sb.append("\r\n\r\n");
        return sb.toString();
    }

    /**
     * 根据 '/' 分割url
     * @param uri
     * @return
     */
    public static String[] getURLPart(String uri) {
        byte startIndex = 0;
        String str = uri.substring(startIndex + 1);
        int endIndex = str.indexOf("/") + 1;
        if(endIndex == -1) {
            return null;
        } else {
            uri.substring(startIndex + 1, endIndex);
            str = uri.substring(endIndex + 1);
            int startIndex1 = str.indexOf("/");
            if(startIndex1 == -1) {
                return null;
            } else {
                String s1 = str.substring(0, startIndex1);
                String s2 = str.substring(startIndex1 + 1);
                int pos = s2.indexOf("/");
                if(pos > -1) {
                    s2 = s2.substring(0, pos);
                }

                return new String[]{s1, s2};
            }
        }
    }
}
