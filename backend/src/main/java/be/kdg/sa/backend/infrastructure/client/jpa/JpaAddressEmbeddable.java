package be.kdg.sa.backend.infrastructure.client.jpa;

import be.kdg.sa.backend.domain.client.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
public class JpaAddressEmbeddable {
    private String street;
    private String streetNumber;
    private String city;
    private String postalCode;
    private String country;

    public JpaAddressEmbeddable() {}

    public JpaAddressEmbeddable(String street, String streetNumber, String city, String postalCode, String country) {

        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public static JpaAddressEmbeddable fromDomain(Address address) {
        if (address == null) {
            return null;
        }
        return new JpaAddressEmbeddable(
                address.getStreet(),
                address.getStreetNumber(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    public Address toDomain() {
        return new Address(
                city,
                streetNumber,
                street,
                postalCode,
                country
        );
    }
}