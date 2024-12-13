package ma.emsi.graphhotel.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.graphhotel.dto.ReservationRequestDTO;
import ma.emsi.graphhotel.dto.ReservationResponseDTO;
import ma.emsi.graphhotel.entities.Chambre;
import ma.emsi.graphhotel.entities.Client;
import ma.emsi.graphhotel.entities.Reservation;
import ma.emsi.graphhotel.mapper.ReservationMapper;
import ma.emsi.graphhotel.repositories.ChambreRepository;
import ma.emsi.graphhotel.repositories.ClientRepository;
import ma.emsi.graphhotel.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService implements ServiceMetier<Reservation, ReservationResponseDTO, ReservationRequestDTO> {

    private final ReservationRepository reservationRepository;
    private final ChambreRepository chambreRepository;
    private final ClientRepository clientRepository;

    @Override
    public Optional<ReservationResponseDTO> findById(Long id) {
        log.info("Fetching reservation with id: {}", id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> {
            log.warn("Reservation with id {} not found", id);
            return new RuntimeException("Reservation with id " + id + " does not exist");
        });
        return Optional.of(ReservationMapper.toResponseDTO(reservation));
    }

    @Override
    public List<ReservationResponseDTO> findAll() {
        log.info("Fetching all reservations");
        return reservationRepository.findAll().stream()
                .map(ReservationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservationResponseDTO> save(ReservationRequestDTO reservationRequestDTO) {
        log.info("Saving new reservation: {}", reservationRequestDTO);

        Chambre chambre = chambreRepository.findById(reservationRequestDTO.chambreId())
                .orElseThrow(() -> {
                    log.error("Chambre with id {} not found", reservationRequestDTO.chambreId());
                    return new RuntimeException("Chambre with id " + reservationRequestDTO.chambreId() + " does not exist");
                });

        if (!chambre.getDisponible()) {
            log.error("Chambre with id {} is not available", chambre.getId());
            throw new RuntimeException("Chambre with id " + chambre.getId() + " is not available");
        }
        Client client = clientRepository.findById(reservationRequestDTO.clientId()).orElseThrow(() -> {
            log.error("client with id {} not found", reservationRequestDTO.clientId());
            return new RuntimeException("client with id " + reservationRequestDTO.clientId() + " does not exist");
        });
        Reservation reservation = Reservation.builder()
                .client(client)
                .chambre(chambre)
                .datedebut(LocalDate.parse(reservationRequestDTO.dateDebut()))
                .datefin(LocalDate.parse(reservationRequestDTO.dateFin()))
                .preferences(reservationRequestDTO.preferences())
                .build();

        chambre.setDisponible(false);
        chambreRepository.save(chambre);

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation saved successfully: {}", savedReservation);
        return Optional.of(ReservationMapper.toResponseDTO(savedReservation));
    }

    @Override
    public Optional<ReservationResponseDTO> update(ReservationRequestDTO reservationRequestDTO, Long id) {
        log.info("Updating reservation with id: {}", id);

        Reservation existingReservation = reservationRepository.findById(id).orElseThrow(() -> {
            log.error("Reservation with id {} not found", id);
            return new RuntimeException("Reservation with id " + id + " does not exist");
        });

        Chambre chambre = null;
        if (reservationRequestDTO.chambreId() != null) {
            chambre = chambreRepository.findById(reservationRequestDTO.chambreId())
                    .orElseThrow(() -> {
                        log.error("Chambre with id {} not found", reservationRequestDTO.chambreId());
                        return new RuntimeException("Chambre with id " + reservationRequestDTO.chambreId() + " does not exist");
                    });

            if (!chambre.getDisponible()) {
                log.error("Chambre with id {} is not available", chambre.getId());
                throw new RuntimeException("Chambre with id " + chambre.getId() + " is not available");
            }
            existingReservation.setChambre(chambre);
        }

        existingReservation.setDatedebut(LocalDate.parse(reservationRequestDTO.dateDebut()));
        existingReservation.setDatefin(LocalDate.parse(reservationRequestDTO.dateFin()));
        existingReservation.setPreferences(reservationRequestDTO.preferences());

        if (chambre != null) {
            chambre.setDisponible(false);
            chambreRepository.save(chambre);
        }

        Reservation updatedReservation = reservationRepository.save(existingReservation);
        log.info("Reservation with id {} updated successfully", id);
        return Optional.of(ReservationMapper.toResponseDTO(updatedReservation));
    }

    @Override
    public Optional<ReservationResponseDTO> delete(Long id) {
        log.info("Deleting reservation with id: {}", id);

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> {
            log.error("Reservation with id {} not found", id);
            return new RuntimeException("Reservation with id " + id + " does not exist");
        });

        // Mark the associated chambre as available when the reservation is deleted
        Chambre chambre = reservation.getChambre();
        if (chambre != null) {
            chambre.setDisponible(true);
            chambreRepository.save(chambre);
            log.info("Chambre with id {} marked as available", chambre.getId());
        }

        reservationRepository.delete(reservation);
        log.info("Reservation with id {} deleted successfully", id);
        return Optional.of(ReservationMapper.toResponseDTO(reservation));
    }
}
