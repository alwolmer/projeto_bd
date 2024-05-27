package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Discard;
import bd20241.Storage.payloads.requests.DiscardItemRequest;
import bd20241.Storage.repositories.DiscardRepository;

@Service
public class DiscardService {
    private final DiscardRepository discardRepository;

    public DiscardService(DiscardRepository discardRepository) {
        this.discardRepository = discardRepository;
    }

    public Discard discardItem(DiscardItemRequest discardItemRequest, String employeeCpf) {
        return discardRepository.save(discardItemRequest, employeeCpf);
    }

    public List<Discard> getDiscards() {
        return discardRepository.findAll();
    }

    public void deleteDiscard(String id) {
        discardRepository.deleteById(id);
    }
    
}
