package be.kdg.sa.backend.infrastructure;

import be.kdg.sa.backend.domain.Order;
import be.kdg.sa.backend.domain.OrderRepository;
import be.kdg.sa.backend.infrastructure.jpa.JpaOrderEntity;
import be.kdg.sa.backend.infrastructure.jpa.JpaOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DbOrderRepository implements OrderRepository {
        private final JpaOrderRepository jpaOrderRepository;

    public DbOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        JpaOrderEntity jpaOrder = JpaOrderEntity.fromDomain(order);
        this.jpaOrderRepository.save(jpaOrder);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return this.jpaOrderRepository.findById(id).map(JpaOrderEntity::toDomain);
    }
}
