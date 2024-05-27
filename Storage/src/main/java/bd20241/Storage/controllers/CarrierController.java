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

import bd20241.Storage.models.Carrier;
import bd20241.Storage.services.CarrierService;

@RestController
@RequestMapping("/carrier")
public class CarrierController {
    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @GetMapping
    public ResponseEntity<List<Carrier>> getAllCarriers() {
        return ResponseEntity.ok(carrierService.getAllCarriers());
    }

    @PostMapping
    public ResponseEntity<Carrier> createCarrier(@RequestBody Carrier carrier) {
        return ResponseEntity.ok(carrierService.createCarrier(carrier));
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> deleteCarrier(@PathVariable String cnpj) {
        String cleanedValue = cnpj.replaceAll("\\D", "").replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        carrierService.deleteCarrier(cleanedValue);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cnpj}")
    public ResponseEntity<Carrier> updateCarrier(@PathVariable String cnpj, @RequestBody Carrier carrier) {
        String cleanedValue = cnpj.replaceAll("\\D", "").replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        carrier.setCnpj(cleanedValue);
        return ResponseEntity.ok(carrierService.updateCarrier(carrier));
    }
}
