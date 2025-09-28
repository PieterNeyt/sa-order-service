package be.kdg.sa.backend.infrastructure;

import be.kdg.sa.backend.domain.OrderRepository;
import be.kdg.sa.backend.infrastructure.jpa.JpaOrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbOrderRepository implements OrderRepository {
        private final JpaOrderRepository jpaOrderRepository;

    public DbOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }
}
