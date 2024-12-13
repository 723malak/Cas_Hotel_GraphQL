package ma.emsi.graphhotel.dto;

public record ChambreRequestDTO(
        Long id,
        String type,
        Double prix,
        Boolean disponible
) {}

