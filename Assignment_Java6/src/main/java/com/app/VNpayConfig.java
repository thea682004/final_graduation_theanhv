package com.app;

import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;

public class VNpayConfig {

    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_RETURN_URL = "http://localhost:8080/order/list";
    public static final String VNP_TMN_CODE = "TX6GE6IQ";
    public static final String SECRET_KEY = "FTKMSHIQHDBIKVBQYWYNOAEMQFZJFWGB";

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException("Key or data is null");
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder(2 * bytes.length);
            for (byte b : bytes) {
                hash.append(String.format("%02x", b & 0xff));
            }
            return hash.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Tạo chuỗi random số với độ dài len
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    // Lấy IP client từ request
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // Tạo hash của tất cả các trường để ký
    public static String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                if (!first) {
                    sb.append("&");
                }
                sb.append(fieldName).append("=").append(fieldValue);
                first = false;
            }
        }
        return hmacSHA512(SECRET_KEY, sb.toString());
    }
}
