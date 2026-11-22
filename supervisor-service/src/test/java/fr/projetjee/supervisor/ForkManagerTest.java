package fr.projetjee.supervisor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForkManagerTest {

    private final ForkManager forkManager = new ForkManager();

    @Test
    void shouldReserveBothForksWhenTheyAreAvailable() {
        assertTrue(forkManager.reserve(1L, 1, 2));
        assertEquals(2, forkManager.getReservations().size());
        assertEquals(1L, forkManager.getReservations().get(1));
        assertEquals(1L, forkManager.getReservations().get(2));
    }

    @Test
    void shouldRejectReservationWhenOneForkIsAlreadyTaken() {
        assertTrue(forkManager.reserve(1L, 1, 2));
        assertFalse(forkManager.reserve(2L, 2, 3));
        assertFalse(forkManager.getReservations().containsKey(3));
    }

    @Test
    void shouldOnlyAllowTheOwnerToReleaseForks() {
        assertTrue(forkManager.reserve(1L, 1, 2));
        assertFalse(forkManager.release(2L, 1, 2));
        assertTrue(forkManager.release(1L, 1, 2));
        assertTrue(forkManager.getReservations().isEmpty());
    }

    @Test
    void shouldRejectTheSameForkTwice() {
        assertFalse(forkManager.reserve(1L, 1, 1));
        assertTrue(forkManager.getReservations().isEmpty());
    }
}