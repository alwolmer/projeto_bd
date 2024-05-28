package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Item;
import bd20241.Storage.payloads.requests.CreateItemRequest;
import bd20241.Storage.repositories.ItemRepository;
import bd20241.Storage.utils.NanoId;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(CreateItemRequest createItemRequest, String currentEmployeeCpf) {
        return itemRepository.save(createItemRequest, currentEmployeeCpf, NanoId.randomNanoIdForStorage());
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }

    public List<Item> getStock() {
        return itemRepository.findStock();
    }
    
}
