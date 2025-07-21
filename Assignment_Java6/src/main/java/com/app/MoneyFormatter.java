package com.app;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component("moneyFormatter")
public class MoneyFormatter {

    public String formatVND(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " ₫";
    }
}
