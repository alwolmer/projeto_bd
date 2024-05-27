package bd20241.Storage.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd20241.Storage.models.DeliveryAddress;
import bd20241.Storage.services.DeliveryAddressService;

@RestController
@RequestMapping("/delivery-address")
public class DeliveryAddressController {
    private final DeliveryAddressService deliveryAddressService;

    public DeliveryAddressController(DeliveryAddressService deliveryAddressService) {
        this.deliveryAddressService = deliveryAddressService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryAddress>> getAllDeliveryAddresses() {
        return ResponseEntity.ok(deliveryAddressService.getAllDeliveryAddresses());
    }

    @PostMapping
    public ResponseEntity<DeliveryAddress> createDeliveryAddress(@RequestBody DeliveryAddress deliveryAddress) {
        return ResponseEntity.ok(deliveryAddressService.createDeliveryAddress(deliveryAddress));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryAddress(@PathVariable String id) {
        deliveryAddressService.deleteDeliveryAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeliveryAddress> updateDeliveryAddress(@PathVariable String id, @RequestBody DeliveryAddress deliveryAddress) {
        deliveryAddress.setId(id);
        return ResponseEntity.ok(deliveryAddressService.updateDeliveryAddress(deliveryAddress));
    }
    
}
