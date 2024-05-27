package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Carrier;
import bd20241.Storage.repositories.CarrierRepository;

@Service
public class CarrierService {
    private final CarrierRepository carrierRepository;

    public CarrierService(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    public void deleteCarrier(String cnpj) {
        carrierRepository.deleteByCnpj(cnpj);
    }

    public Carrier updateCarrier(Carrier updatedCarrier) {
        carrierRepository.updateCarrier(updatedCarrier);
        return updatedCarrier;
    }

    public Carrier getCarrierByCnpj(String cnpj) {
        return carrierRepository.findByCnpj(cnpj);
    }

    public List<Carrier> getAllCarriers() {
        return carrierRepository.findAll();
    }

    public Carrier createCarrier(Carrier carrier) {
        carrierRepository.save(carrier);
        return carrier;
    }
    
}
