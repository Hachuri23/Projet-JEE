package fr.projetjee.supervisor;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class ForkManager {

    private final Map<Integer, Long> reservations = new TreeMap<>();

    public synchronized boolean reserve(Long philosopherId, Integer firstFork, Integer secondFork) {
        if (philosopherId == null || firstFork == null || secondFork == null) {
            return false;
        }

        if (firstFork.equals(secondFork)) {
            return false;
        }

        if (reservations.containsKey(firstFork) || reservations.containsKey(secondFork)) {
            return false;
        }

        reservations.put(firstFork, philosopherId);
        reservations.put(secondFork, philosopherId);
        return true;
    }

    public synchronized boolean release(Long philosopherId, Integer firstFork, Integer secondFork) {
        if (philosopherId == null || firstFork == null || secondFork == null) {
            return false;
        }

        if (!philosopherId.equals(reservations.get(firstFork))
                || !philosopherId.equals(reservations.get(secondFork))) {
            return false;
        }

        reservations.remove(firstFork);
        reservations.remove(secondFork);
        return true;
    }

    public synchronized Map<Integer, Long> getReservations() {
        return Map.copyOf(reservations);
    }
}