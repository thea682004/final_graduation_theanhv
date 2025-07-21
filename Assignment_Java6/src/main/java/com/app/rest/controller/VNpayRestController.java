package com.app.rest.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.VNpayConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class VNpayRestController {

    @PostMapping("/createPayment")
    public void createPayment(@RequestParam("amount") int amount,
                              @RequestParam(value = "bankCode", required = false) String bankCode,
                              @RequestParam(value = "language", defaultValue = "vn") String language,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_TxnRef = VNpayConfig.getRandomNumber(8);
        String vnp_TmnCode = VNpayConfig.VNP_TMN_CODE;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));  // amount nhân 100 theo yêu cầu
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.trim().isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode.trim());
        }

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh_toan_don_hang_" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", language);
        vnp_Params.put("vnp_ReturnUrl", VNpayConfig.VNP_RETURN_URL);

        // Thời gian tạo đơn
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Thời gian hết hạn (15 phút sau)
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Sắp xếp và tạo chuỗi hashData + query string
        List<String> sortedKeys = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(sortedKeys);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        int count = 0;
        for (String key : sortedKeys) {
            String value = vnp_Params.get(key);
            if (value != null && !value.isEmpty()) {
                if (count > 0) {
                    hashData.append('&');
                    query.append('&');
                }
                hashData.append(key).append('=').append(value);
                query.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                     .append('=')
                     .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                count++;
            }
        }

        // Tạo secure hash
        String vnp_SecureHash = VNpayConfig.hmacSHA512(VNpayConfig.SECRET_KEY, hashData.toString());
        query.append("&vnp_SecureHash=").append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.UTF_8));

        // Tạo URL thanh toán hoàn chỉnh
        String paymentUrl = VNpayConfig.VNP_PAY_URL + "?" + query.toString();

        // Trả về JSON kết quả
        JSONObject job = new JSONObject();
        job.put("code", "00");
        job.put("message", "success");
        job.put("data", paymentUrl);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(job.toString());

        // Debug log
        System.out.println("HashData: " + hashData.toString());
        System.out.println("SecureHash: " + vnp_SecureHash);
        System.out.println("Payment URL: " + paymentUrl);
    }
}
