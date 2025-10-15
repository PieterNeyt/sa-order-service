package be.kdg.sa.backend.infrastructure.order.jpa;

import be.kdg.sa.backend.domain.client.Address;
import be.kdg.sa.backend.domain.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "clients")
public class JpaClientEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID clientId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false)
    private JpaAddressEntity address;

    public JpaClientEntity() {}

    public JpaClientEntity(UUID clientId, String firstName, String lastName, String email, String phoneNumber, Date birthDate, JpaAddressEntity address) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
    }

    public static JpaClientEntity fromDomain(Client client) {
        JpaAddressEntity addressEntity = JpaAddressEntity.fromDomain(client.getAddress());

        return new JpaClientEntity(
                client.getClientId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getBirthDate(),
                addressEntity
        );
    }

    public Client toDomain() {
        Address domainAddress = address.toDomain();

        Client client = new Client(
                domainAddress,
                birthDate,
                email,
                firstName,
                lastName,
                phoneNumber
        );
        client.setClientId(clientId);
        return client;
    }
}