package be.kdg.sa.backend.domain.client;

import java.util.Optional;
import java.util.UUID;


public interface ClientRepository {
    Optional<Client> findById(UUID clientId);
    Client save(Client client);
}