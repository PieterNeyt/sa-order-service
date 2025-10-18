package be.kdg.sa.backend.infrastructure.client.jpa;

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


    private String firstName;


    private String lastName;

    @Column(unique = true)
    private String email;


    private String phoneNumber;


    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Embedded
    private JpaAddressEmbeddable address;

    public JpaClientEntity() {
    }

    public JpaClientEntity(UUID clientId, String firstName, String lastName, String email, String phoneNumber, Date birthDate, JpaAddressEmbeddable address) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
    }

    public static JpaClientEntity fromDomain(Client client) {
        JpaAddressEmbeddable addressEmbeddable = null;
        if (client.getAddress() != null) {
            addressEmbeddable = JpaAddressEmbeddable.fromDomain(client.getAddress());
        }

        return new JpaClientEntity(
                client.getClientId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getBirthDate(),
                addressEmbeddable
        );
    }

    public Client toDomain() {
        Address domainAddress = null;
        if (address != null) {
            domainAddress = address.toDomain();
        }

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