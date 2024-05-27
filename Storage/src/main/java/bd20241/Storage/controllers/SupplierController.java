package bd20241.Storage.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd20241.Storage.models.Supplier;
import bd20241.Storage.payloads.requests.EditSupplierRequest;
import bd20241.Storage.services.SupplierService;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    @PatchMapping("/{cnpj}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable String cnpj, @RequestBody EditSupplierRequest supplier) {
        String cleanedValue = cnpj.replaceAll("\\D", "").replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");


        Supplier updatedSupplier = supplierService.getSupplierByCnpj(cleanedValue);
        if (updatedSupplier == null) {
            return ResponseEntity.notFound().build();
        }
        updatedSupplier = supplierService.updateSupplier(supplier, cleanedValue);

        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String cnpj) {
        String cleanedValue = cnpj.replaceAll("\\D", "").replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");

        Supplier supplier = supplierService.getSupplierByCnpj(cleanedValue);
        if (supplier == null) {
            return ResponseEntity.notFound().build();
        }
        supplierService.deleteSupplier(cleanedValue);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
