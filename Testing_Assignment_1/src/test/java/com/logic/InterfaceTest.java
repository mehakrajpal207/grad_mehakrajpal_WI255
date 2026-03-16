package com.logic;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InterfaceTest {

    @Test
    void testInterfaceMethod() {
        // 1. Create Mock (Tests interface without class A)
        I mockI = Mockito.mock(I.class);

        // 2. Call the void method 3 times
        mockI.abc();
        mockI.abc();
        mockI.abc();

        // 3. Verify it was called exactly 3 times
        verify(mockI, times(3)).abc();
    }
}