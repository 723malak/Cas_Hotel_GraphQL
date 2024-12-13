package ma.emsi.graphhotel.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.graphhotel.dto.ChambreRequestDTO;
import ma.emsi.graphhotel.dto.ChambreResponseDTO;
import ma.emsi.graphhotel.services.ChambreService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChambreController {

    private final ChambreService chambreService;

    @QueryMapping
    public List<ChambreResponseDTO> findAllChambres() {
        return chambreService.findAll();
    }

    @QueryMapping
    public ChambreResponseDTO findChambreById(@Argument Long id) {
        return chambreService.findById(id).orElseThrow(() -> new RuntimeException("Chambre not found"));
    }

    @QueryMapping
    public List<ChambreResponseDTO> findChambresByDisponibilite(@Argument boolean disponible) {
        return chambreService.findByDisponibilite(disponible);
    }

    @MutationMapping
    public ChambreResponseDTO createChambre(@Argument ChambreRequestDTO chambre) {
        return chambreService.save(chambre).orElseThrow(() -> new RuntimeException("Error while creating chambre"));
    }

    @MutationMapping
    public ChambreResponseDTO updateChambre(@Argument Long id, @Argument ChambreRequestDTO chambre) {
        return chambreService.update(chambre, id).orElseThrow(() -> new RuntimeException("Error while updating chambre"));
    }

    @MutationMapping
    public ChambreResponseDTO deleteChambre(@Argument Long id) {
        return chambreService.delete(id).orElseThrow(() -> new RuntimeException("Error while deleting chambre"));
    }
}
