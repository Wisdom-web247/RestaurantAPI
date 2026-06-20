package com.restaurant.api.controller;

import com.restaurant.api.model.Location;
import com.restaurant.api.repository.LocationRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationRepository repo;
    public LocationController(LocationRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Location> getAll() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Location create(@Valid @RequestBody Location l) { l.setId(null); return repo.save(l); }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id, @Valid @RequestBody Location body) {
        return repo.findById(id).map(l -> {
            body.setId(id);
            return ResponseEntity.ok(repo.save(body));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
