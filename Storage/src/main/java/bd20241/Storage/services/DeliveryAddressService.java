package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.DeliveryAddress;
import bd20241.Storage.repositories.DeliveryAddressRepository;
import bd20241.Storage.utils.NanoId;

@Service
public class DeliveryAddressService {
    private final DeliveryAddressRepository deliveryAddressRepository;

    public DeliveryAddressService(DeliveryAddressRepository deliveryAddressRepository) {
        this.deliveryAddressRepository = deliveryAddressRepository;
    }

    public void deleteDeliveryAddress(String id) {
        deliveryAddressRepository.deleteById(id);
    }

    public DeliveryAddress updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        deliveryAddressRepository.updateDeliveryAddress(deliveryAddress);
        return deliveryAddress;
    }

    public DeliveryAddress getDeliveryAddressById(String id) {
        return deliveryAddressRepository.findById(id);
    }

    public DeliveryAddress createDeliveryAddress(DeliveryAddress deliveryAddress) {
        deliveryAddress.setId(NanoId.randomNanoIdForStorage());
        deliveryAddressRepository.save(deliveryAddress);
        return deliveryAddress;
    }

    public List<DeliveryAddress> getAllDeliveryAddresses() {
        return deliveryAddressRepository.findAll();
    }
    
}
