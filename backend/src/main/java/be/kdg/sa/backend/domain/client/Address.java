package be.kdg.sa.backend.domain.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import lombok.EqualsAndHashCode; // Nieuwe import
import lombok.Getter;
// import lombok.Setter; // Verwijder deze import
import lombok.ToString;
import org.jmolecules.ddd.annotation.ValueObject; // Nieuwe import


@ValueObject
@Getter
@ToString
@EqualsAndHashCode
public class Address {

    private final String street;
    private final String streetNumber;
    private final String city;
    private final String postalCode;
    private final String country;

    public Address(String city, String streetNumber, String street, String postalCode, String country) {
        this.city = city;
        this.streetNumber = streetNumber;
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
    }
}