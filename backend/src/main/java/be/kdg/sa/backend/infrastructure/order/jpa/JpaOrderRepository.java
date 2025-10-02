package be.kdg.sa.backend.infrastructure.order.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID>  {
}
