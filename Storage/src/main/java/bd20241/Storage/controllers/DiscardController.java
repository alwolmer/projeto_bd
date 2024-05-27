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

import bd20241.Storage.models.Discard;
import bd20241.Storage.payloads.requests.DiscardItemRequest;
import bd20241.Storage.services.DiscardService;

@RestController
@RequestMapping("/discard")
public class DiscardController {
    private final DiscardService discardService;

    public DiscardController(DiscardService discardService) {
        this.discardService = discardService;
    }

    @PostMapping
    public ResponseEntity<Discard> discard(@RequestBody DiscardItemRequest discardItemRequest) {
        UserDetails user =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String cpf = user.getUsername();
        Discard discarded = discardService.discardItem(discardItemRequest, cpf);
        return ResponseEntity.ok(discarded);
    }

    @GetMapping
    public ResponseEntity<List<Discard>> getDiscards() {
        return ResponseEntity.ok(discardService.getDiscards());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscard(@PathVariable String id) {
        discardService.deleteDiscard(id);
        return ResponseEntity.noContent().build();
    }
    
}
