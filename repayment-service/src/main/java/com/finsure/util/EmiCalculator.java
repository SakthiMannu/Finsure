package com.finsure.util;
/*
EMI = P × r × (1 + r)^n
      ─────────────────
         (1 + r)^n - 1

P->Principal (loan amount)
r->Monthly interest rate
n->Tenure in months
 */
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
