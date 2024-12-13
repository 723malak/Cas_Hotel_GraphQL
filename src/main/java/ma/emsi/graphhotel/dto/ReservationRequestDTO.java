package ma.emsi.graphhotel.dto;



public record ReservationRequestDTO(
        Long clientId,
        Long chambreId,
        String dateDebut,
        String dateFin,
        String preferences
) {}
