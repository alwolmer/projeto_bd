package bd20241.Storage.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd20241.Storage.models.Item;
import bd20241.Storage.payloads.requests.CreateItemRequest;
import bd20241.Storage.payloads.responses.StockStatsResponse;
import bd20241.Storage.services.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemService.getItems());
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody CreateItemRequest item) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentCpf = userDetails.getUsername();
        return ResponseEntity.ok(itemService.createItem(item, currentCpf));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock")
    public ResponseEntity<List<Item>> getStock() {
        return ResponseEntity.ok(itemService.getStock());
    }

    @GetMapping("/stock/stats")
    public ResponseEntity<List<StockStatsResponse>> getStockStats() {
        return ResponseEntity.ok(itemService.getStockStats());
    }
    
}
