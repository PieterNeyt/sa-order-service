package be.kdg.sa.backend.infrastructure.jpa;

import be.kdg.sa.backend.domain.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID>  {
}
