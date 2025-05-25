package com.example.demo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project: demoDarbyFrameworks2-master
 * Package: com.example.demo.domain
 */
class PartTest {
    Part partIn;
    Part partOut;

    @BeforeEach
    void setUp() {
        partIn = new InhousePart();
        partOut = new OutsourcedPart();
    }
    @Test
    void testMinInventoryValidation() {
        partIn.setMinInv(10);
        partIn.setMaxInv(50);  // Ensure maxInv is set
        partIn.setInv(10);     // Should succeed
        assertEquals(10, partIn.getInv());
    }

    @Test
    void testMaxInventoryValidation() {
        partIn.setMinInv(10);
        partIn.setMaxInv(50);  // Ensure minInv is set
        partIn.setInv(50);     // Should succeed
        assertEquals(50, partIn.getInv());
    }



    @Test
    void testInventoryBelowMin() {
        partIn.setMinInv(10);
        partIn.setMaxInv(50);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            partIn.setInv(5);  // Should throw an error because it's below 10
        });

        assertEquals("Inventory must be at least 10", exception.getMessage());
    }

    @Test
    void testInventoryAboveMax() {
        partIn.setMinInv(5);
        partIn.setMaxInv(30);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            partIn.setInv(35);  // This should throw an error because 35 > maxInv (30)
        });

        assertEquals("Inventory cannot exceed 30", exception.getMessage());  // Check exception message
    }

}

