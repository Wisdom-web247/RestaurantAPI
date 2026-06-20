package com.restaurant.api.controller;

import com.restaurant.api.model.Reservation;
import com.restaurant.api.repository.ReservationRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository repo;

    public ReservationController(ReservationRepository repo) {
        this.repo = repo;
    }

    // GET all (optional ?status=PENDING)
    @GetMapping
    public List<Reservation> getAll(@RequestParam(required = false) String status) {
        return (status != null) ? repo.findByStatus(status) : repo.findAll();
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create
    @PostMapping
    public Reservation create(@Valid @RequestBody Reservation r) {
        r.setId(null);
        if (r.getStatus() == null) r.setStatus("PENDING");
        return repo.save(r);
    }

    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @Valid @RequestBody Reservation r) {
        return repo.findById(id).map(existing -> {
            existing.setCustomerName(r.getCustomerName());
            existing.setEmail(r.getEmail());
            existing.setPhone(r.getPhone());
            existing.setReservationTime(r.getReservationTime());
            existing.setPartySize(r.getPartySize());
            existing.setSpecialRequests(r.getSpecialRequests());
            existing.setStatus(r.getStatus());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PATCH status only (handy for Confirm/Cancel buttons later)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Reservation> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return repo.findById(id).map(existing -> {
            existing.setStatus(status);
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}