package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Supplier;
import bd20241.Storage.payloads.requests.EditSupplierRequest;
import bd20241.Storage.repositories.SupplierRepository;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
        return supplier;
    }

    public Supplier getSupplierByCnpj(String cnpj) {
        return supplierRepository.findByCnpj(cnpj);
    }

    public Supplier updateSupplier(EditSupplierRequest supplier, String cnpj) {
        Supplier newSupplier = new Supplier();
        newSupplier.setName(supplier.getName());
        newSupplier.setEmail(supplier.getEmail());
        newSupplier.setPhone(supplier.getPhone());
        newSupplier.setCnpj(cnpj);

        supplierRepository.updateSupplier(newSupplier);
        return newSupplier;
    }

    public void deleteSupplier(String cnpj) {
        supplierRepository.deleteByCnpj(cnpj);
    }
    
}
