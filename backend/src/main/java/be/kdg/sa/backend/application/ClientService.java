package be.kdg.sa.backend.application;

import be.kdg.sa.backend.api.dto.ClientDto;
import be.kdg.sa.backend.api.dto.OrderInformationDto;
import be.kdg.sa.backend.domain.client.Address;
import be.kdg.sa.backend.domain.client.Client;
import be.kdg.sa.backend.domain.client.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public void saveOrUpdateClient(UUID clientId, OrderInformationDto orderInformation) {

        String[] nameParts = orderInformation.name().trim().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Zoek bestaande client of maak nieuwe
        Client client = clientRepository.findById(clientId)
                .orElse(createNewClient(clientId));

        //:TODO client aanpassen
    /*    client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(orderInformation.email());

        Address address = new Address(
                orderInformation.city(),
                "12",
                orderInformation.street(),
                orderInformation.postalcode(),
                "Belgium"
        );
        client.setAddress(address);*/

        clientRepository.save(client);
    }


    public ClientDto login(UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client niet gevonden: " + clientId));

        return ClientDto.from(client);
    }


    private Client createNewClient(UUID clientId) {
        Client client = new Client(null, null, "", "", "", "");
        client.changeClientId(clientId);
        return client;
    }
}
