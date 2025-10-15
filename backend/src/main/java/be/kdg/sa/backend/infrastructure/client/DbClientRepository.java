package be.kdg.sa.backend.infrastructure.client;

import be.kdg.sa.backend.domain.client.Client;
import be.kdg.sa.backend.domain.client.ClientRepository;

import be.kdg.sa.backend.infrastructure.client.jpa.JpaClientEntity;
import be.kdg.sa.backend.infrastructure.client.jpa.JpaClientRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class DbClientRepository implements ClientRepository {
    private final JpaClientRepository jpaClientRepository;

    public DbClientRepository(JpaClientRepository jpaClientRepository) {
        this.jpaClientRepository = jpaClientRepository;
    }

    @Override
    public Optional<Client> findById(UUID clientId) {
        return jpaClientRepository.findById(clientId)
                .map(JpaClientEntity::toDomain);
    }

    @Override
    public Client save(Client client) {
        JpaClientEntity entity = JpaClientEntity.fromDomain(client);
        JpaClientEntity saved = jpaClientRepository.save(entity);
        return saved.toDomain();
    }
}