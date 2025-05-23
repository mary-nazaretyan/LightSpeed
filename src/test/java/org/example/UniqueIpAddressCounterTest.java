package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests methods within {@link UniqueIpAddressCounter} class.
 */
public class UniqueIpAddressCounterTest {
    @ParameterizedTest
    @CsvSource({ "0.0.0.0, 0", "10.0.0.1, 167772161", "255.255.255.255, -1" })
    void testIpToInt(String ip, int expected) {
        // when
        int result = UniqueIpAddressCounter.ipToInt(ip);
        // then
        assertEquals(expected, result);
    }

    @Test
    public void shouldThrowExceptionWhenIpIsInvalid() {
        // given
        String ipAddress = "0.0.0.0.0";
        // when, then
        assertThrows(IllegalArgumentException.class, () -> UniqueIpAddressCounter.ipToInt(ipAddress));
    }
    // ToDo: Add tests for methods createBufferedReader(), count()
}
