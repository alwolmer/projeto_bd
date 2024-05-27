package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Client;
import bd20241.Storage.repositories.ClientRepository;
import bd20241.Storage.utils.NanoId;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        client.setId(NanoId.randomNanoIdForStorage());
        clientRepository.save(client);
        return client;
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(id);
    }

    public Client updateClient(Client client) {
        clientRepository.updateClient(client);
        return client;
    }

    public Client getClientById(String id) {
        return clientRepository.findById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
}
