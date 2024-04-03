package org.bansikah.junittestcalculator;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    Calculator calculator;

    @Test
    public void testAdd() {
        calculator = new Calculator();
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    public void testSubtract() {
        calculator = new Calculator();
        assertEquals(2, calculator.subtract(5, 3));
    }

    @Test
    public void testMultiply() {
        calculator = new Calculator();
        assertEquals(6, calculator.multiply(2, 3));
    }

    @Test
    public void testDivide() {
        calculator = new Calculator();
        assertEquals(2, calculator.divide(6, 3));
    }
}
