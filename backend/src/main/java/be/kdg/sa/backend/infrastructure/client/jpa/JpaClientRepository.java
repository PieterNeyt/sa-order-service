package be.kdg.sa.backend.infrastructure.client.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaClientRepository extends JpaRepository<JpaClientEntity, UUID> {
}