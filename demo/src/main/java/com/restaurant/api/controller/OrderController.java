package com.restaurant.api.controller;

import com.restaurant.api.model.Order;
import com.restaurant.api.model.OrderItem;
import com.restaurant.api.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository repo;
    public OrderController(OrderRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Order> getAll(@RequestParam(required = false) String status) {
        return status == null ? repo.findAll() : repo.findByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order create(@Valid @RequestBody Order order) {
        order.setId(null);
        order.setCreatedAt(LocalDateTime.now());
        if (order.getStatus() == null) order.setStatus("PENDING");

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem it : order.getItems()) {
            it.setId(null);
            it.setOrder(order);
            total = total.add(it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
        }
        order.setTotal(total);
        return repo.save(order);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return repo.findById(id).map(o -> {
            o.setStatus(body.get("status"));
            return ResponseEntity.ok(repo.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}