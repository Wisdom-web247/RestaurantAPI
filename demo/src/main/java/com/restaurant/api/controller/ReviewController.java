package com.restaurant.api.controller;

import com.restaurant.api.model.Review;
import com.restaurant.api.repository.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository repo;
    public ReviewController(ReviewRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Review> getAll() { return repo.findAllByOrderByCreatedAtDesc(); }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Review create(@Valid @RequestBody Review r) {
        r.setId(null);
        r.setCreatedAt(LocalDateTime.now());
        return repo.save(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
