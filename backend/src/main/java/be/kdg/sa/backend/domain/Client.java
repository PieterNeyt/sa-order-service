package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Client {
    public UUID clientId;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public Address address;
    public Date birthDate;

    public List<OrderOccasion> orderOccasions;

    public Client(Address address, Date birthDate, String email, String firstName, String lastName, String phoneNumber) {
        this.clientId = UUID.randomUUID();
        this.address = address;
        this.birthDate = birthDate;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.orderOccasions = new ArrayList<>();
    }
}
