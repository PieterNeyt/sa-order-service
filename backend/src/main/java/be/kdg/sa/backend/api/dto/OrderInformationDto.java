package be.kdg.sa.backend.api.dto;

public record OrderInformationDto(
        String name,
        String email,
        String street,
        String postalcode,
        String city
) {}
