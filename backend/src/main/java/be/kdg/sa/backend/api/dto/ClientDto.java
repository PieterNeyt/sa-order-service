package be.kdg.sa.backend.api.dto;

import be.kdg.sa.backend.domain.client.Client;

public record ClientDto(String id, String firstName, String lastName, String email) {
    public static ClientDto from(Client client) {
        return new ClientDto(
                client.getClientId().toString(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail()
        );
    }
}