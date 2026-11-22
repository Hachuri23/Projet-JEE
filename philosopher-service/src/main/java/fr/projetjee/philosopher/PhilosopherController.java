package fr.projetjee.philosopher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/philosophers")
public class PhilosopherController {

    private final RestTemplate restTemplate;
    private final String supervisorUrl;

    public PhilosopherController(
            RestTemplate restTemplate,
            @Value("${supervisor.url}") String supervisorUrl
    ) {
        this.restTemplate = restTemplate;
        this.supervisorUrl = supervisorUrl;
    }

    @PostMapping("/{id}/eat")
    public ResponseEntity<String> eat(
            @PathVariable Long id,
            @RequestParam Integer firstFork,
            @RequestParam Integer secondFork
    ) {
        ReservationRequest request = new ReservationRequest(id, firstFork, secondFork);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    supervisorUrl + "/api/supervisor/reservations",
                    request,
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<String> finish(
            @PathVariable Long id,
            @RequestParam Integer firstFork,
            @RequestParam Integer secondFork
    ) {
        ReservationRequest request = new ReservationRequest(id, firstFork, secondFork);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    supervisorUrl + "/api/supervisor/reservations",
                    HttpMethod.DELETE,
                    new HttpEntity<>(request),
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsString());
        }
    }

    private record ReservationRequest(
            Long philosopherId,
            Integer firstFork,
            Integer secondFork
    ) {
    }
}