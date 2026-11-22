package fr.projetjee.supervisor;

public record ReservationRequest(
        Long philosopherId,
        Integer firstFork,
        Integer secondFork
) {
}