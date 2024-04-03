package org.bansikah.junittestcalculator;

import org.junit.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.Assert.assertTrue;

public class JUnitTestExample {
    @Test
    @Tag("Unit test")
    public void test() {
        assertTrue(true);
    }
}
