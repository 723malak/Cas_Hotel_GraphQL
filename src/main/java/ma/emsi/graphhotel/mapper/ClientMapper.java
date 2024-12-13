package ma.emsi.graphhotel.mapper;

import ma.emsi.graphhotel.dto.ClientRequestDTO;
import ma.emsi.graphhotel.dto.ClientResponseDTO;
import ma.emsi.graphhotel.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public static ClientResponseDTO toResponseDTO(Client client) {
        return ClientResponseDTO.builder()
                .id(client.getId())
                .nom(client.getNom())
                .email(client.getEmail())
                .tel(client.getTel())
                .build();
    }

    public static Client toEntity(ClientRequestDTO dto) {
        return Client.builder()
                .nom(dto.nom())
                .email(dto.email())
                .tel(dto.tel())
                .build();
    }
}
