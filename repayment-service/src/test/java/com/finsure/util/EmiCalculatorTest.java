package com.finsure.util;

import com.finsure.util.EmiCalculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmiCalculatorTest {

    @Test
    void testEmi_50000() {
        double emi = EmiCalculator.calculateMonthlyEmi(50000);
        assertEquals(4442.44, emi, 0.01);
    }

    // Total repayable = EMI × 12
    @Test
    void testTotalRepayable_50000() {
        double emi           = EmiCalculator.calculateMonthlyEmi(50000);
        double totalRepayable = Math.round(emi * 12 * 100.0) / 100.0;
        assertEquals(53309.28, totalRepayable, 0.01);
    }

    // Total repayable must be MORE than principal (interest added)
    @Test
    void testTotalRepayable_MoreThanPrincipal() {
        double emi           = EmiCalculator.calculateMonthlyEmi(50000);
        double totalRepayable = emi * 12;
        assertTrue(totalRepayable > 50000);
    }

    // EMI should be positive
    @Test
    void testEmi_IsPositive() {
        double emi = EmiCalculator.calculateMonthlyEmi(50000);
        assertTrue(emi > 0);
    }

    // EMI for 200000 = exactly 2 × EMI for 100000
    @Test
    void testEmi_LinearScaling() {
        double emi100k = EmiCalculator.calculateMonthlyEmi(100000);
        double emi200k = EmiCalculator.calculateMonthlyEmi(200000);
        assertEquals(emi100k * 2, emi200k, 0.01);
    }

}
