package org.example.devices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StarterTests {

    @Test
    void testMinimalClass() {

        Starter minimal = new Starter();
        assertNotNull(minimal);
    }
}
