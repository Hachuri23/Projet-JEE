package fr.projetjee.supervisor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    private final ForkManager forkManager;

    public SupervisorController(ForkManager forkManager) {
        this.forkManager = forkManager;
    }

    @PostMapping("/reservations")
    public ResponseEntity<String> reserve(@RequestBody ReservationRequest request) {
        boolean reserved = forkManager.reserve(
                request.philosopherId(),
                request.firstFork(),
                request.secondFork()
        );

        if (!reserved) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Les deux fourchettes ne sont pas disponibles.");
        }

        return ResponseEntity.ok("Fourchettes réservées.");
    }

    @DeleteMapping("/reservations")
    public ResponseEntity<String> release(@RequestBody ReservationRequest request) {
        boolean released = forkManager.release(
                request.philosopherId(),
                request.firstFork(),
                request.secondFork()
        );

        if (!released) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Libération refusée : réservation introuvable ou propriétaire différent.");
        }

        return ResponseEntity.ok("Fourchettes libérées.");
    }

    @GetMapping("/forks")
    public Map<Integer, Long> getForks() {
        return forkManager.getReservations();
    }
}