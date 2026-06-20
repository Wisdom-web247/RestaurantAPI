package com.restaurant.api.controller;

import com.restaurant.api.model.MenuItem;
import com.restaurant.api.repository.MenuItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

    private final MenuItemRepository repo;

    public MenuItemController(MenuItemRepository repo) {
        this.repo = repo;
    }

    // GET /api/menu  -> all items
    // GET /api/menu?category=Main -> filter by category
    @GetMapping
    public List<MenuItem> list(@RequestParam(required = false) String category) {
        if (category != null) return repo.findByCategory(category);
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MenuItem create(@RequestBody MenuItem item) {
        return repo.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> update(@PathVariable Long id, @RequestBody MenuItem item) {
        return repo.findById(id).map(existing -> {
            item.setId(id);
            return ResponseEntity.ok(repo.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
