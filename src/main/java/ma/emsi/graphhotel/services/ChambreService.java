package ma.emsi.graphhotel.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.graphhotel.dto.ChambreRequestDTO;
import ma.emsi.graphhotel.dto.ChambreResponseDTO;
import ma.emsi.graphhotel.entities.Chambre;
import ma.emsi.graphhotel.entities.Reservation;
import ma.emsi.graphhotel.entities.TypeChambre;
import ma.emsi.graphhotel.mapper.ChambreMapper;
import ma.emsi.graphhotel.repositories.ChambreRepository;
import ma.emsi.graphhotel.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChambreService implements ServiceMetier<Chambre, ChambreResponseDTO, ChambreRequestDTO> {

    private final ChambreRepository chambreRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Optional<ChambreResponseDTO> findById(Long id) {
        log.info("Fetching chambre with id: {}", id);
        Chambre result=chambreRepository.findById(id).orElseThrow(() -> {
            log.error("Chambre with id {} not found", id);
            return new RuntimeException("Chambre with id " + id + " does not exist");
        });
        return Optional.of(ChambreMapper.toResponseDTO(result));
    }

    @Override
    public List<ChambreResponseDTO> findAll() {
        log.info("Fetching all chambres");
        return chambreRepository.findAll().stream().map(ChambreMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ChambreResponseDTO> save(ChambreRequestDTO chambre) {
        if (chambre.id() != null && chambreRepository.existsById(chambre.id())) {
            log.error("Chambre with id {} already exists", chambre.id());
            throw new RuntimeException("Chambre with id " + chambre.id() + " already exists");
        }
        Chambre saved =ChambreMapper.toEntity(chambre);
        log.info("Saving new chambre: {}", chambre);
        Chambre result=chambreRepository.save(saved);
        return Optional.of(ChambreMapper.toResponseDTO(result));
    }

    @Override
    public Optional<ChambreResponseDTO> update(ChambreRequestDTO chambre, Long id) {
        log.info("Updating chambre with id: {}", id);
        Chambre existingChambre = chambreRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chambre with id {} not found", id);
                    return new RuntimeException("Chambre with id " + id + " does not exist");
                });
        if (chambre.prix()!=null && !chambre.prix().equals(existingChambre.getPrix())){
            existingChambre.setPrix(chambre.prix());

        }
        if (chambre.disponible()!=null && !chambre.disponible().equals(existingChambre.getDisponible())){
            existingChambre.setDisponible(chambre.disponible());

        }
        if (chambre.type() != null){
            existingChambre.setType(TypeChambre.valueOf(chambre.type()));

        }
        log.info("Chambre with id {} updated successfully", id);
        Chambre result=chambreRepository.save(existingChambre);
        return Optional.of(ChambreMapper.toResponseDTO(result));
    }

    @Override
    public Optional<ChambreResponseDTO> delete(Long id) {
        log.info("Deleting chambre with id: {}", id);
        Chambre existingChambre = chambreRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chambre with id {} not found", id);
                    return new RuntimeException("Chambre with id " + id + " does not exist");
                });
        List<Reservation> reservations = reservationRepository.findByChambre(existingChambre);
        log.info("Found {} reservations associated with chambre id: {}", reservations.size(), id);
        for (Reservation reservation : reservations) {
            log.info("Deleting reservation with id: {}", reservation.getId());
            reservationRepository.delete(reservation);
        }
        chambreRepository.delete(existingChambre);
        log.info("Chambre with id {} deleted successfully", id);
        return Optional.of(ChambreMapper.toResponseDTO(existingChambre));
    }

    public List<ChambreResponseDTO> findByDisponibilite(boolean isDisponible) {
        log.info("Fetching chambres with disponibilité: {}", isDisponible);
        List<Chambre> chambres = chambreRepository.findByDisponible(isDisponible);
        log.info("Found {} chambres with disponibilité: {}", chambres.size(), isDisponible);
        return chambres.stream().map(ChambreMapper::toResponseDTO).collect(Collectors.toList());
    }
}
