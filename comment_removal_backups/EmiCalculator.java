package com.finsure.util;

public class EmiCalculator {

    private static final double ANNUAL_INTEREST_RATE = 12.0;

    public static double calculateMonthlyEmi(double principal) {

        int tenureMonths = 12;
        double monthlyRate = ANNUAL_INTEREST_RATE / (12 * 100);

        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)) /
                (Math.pow(1 + monthlyRate, tenureMonths) - 1);

        return Math.round(emi * 100.0) / 100.0;
    }
}
