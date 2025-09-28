package be.kdg.sa.backend.infrastructure.jpa;

import jakarta.persistence.Table;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

@Entity
@Getter
@Table(name="order")
public class JpaOrderEntity {
}
