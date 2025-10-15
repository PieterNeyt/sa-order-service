package be.kdg.sa.backend.infrastructure.client.jpa;

import be.kdg.sa.backend.domain.client.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class JpaAddressEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID addressId;


    private String street;


    private String streetNumber;


    private String city;


    private String postalCode;


    private String country;

    public JpaAddressEntity() {}

    public JpaAddressEntity(UUID addressId, String street, String streetNumber, String city, String postalCode, String country) {
        this.addressId = addressId;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public static JpaAddressEntity fromDomain(Address address) {
        if (address == null) {
            return null;
        }
        return new JpaAddressEntity(
                address.getAddressId(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    public Address toDomain() {
        Address address = new Address(
                city,
                streetNumber,
                street,
                postalCode,
                country
        );
        address.setAddressId(addressId);
        return address;
    }
}