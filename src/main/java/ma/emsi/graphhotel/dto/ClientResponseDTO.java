package ma.emsi.graphhotel.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseDTO {
    private Long id;
    private String nom;
    private String email;
    private String tel;
}
