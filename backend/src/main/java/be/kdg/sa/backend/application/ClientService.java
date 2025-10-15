package be.kdg.sa.backend.application;

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

        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(orderInformation.email());

        // Update of maak Address
        if (client.getAddress() == null) {
            Address address = new Address(
                    orderInformation.city(),
                    "12",
                    orderInformation.street(),
                    orderInformation.postalcode(),
                    "Belgium"
            );
            client.setAddress(address);
        } else {
            Address address = client.getAddress();
            address.setStreet(orderInformation.street());
            address.setPostalCode(orderInformation.postalcode());
            address.setCity(orderInformation.city());
        }

        clientRepository.save(client);
    }

    private Client createNewClient(UUID clientId) {
        Client client = new Client(null, null, "", "", "", "");
        client.setClientId(clientId);
        return client;
    }
}
