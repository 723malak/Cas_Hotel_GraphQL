package ma.emsi.graphhotel;

import lombok.RequiredArgsConstructor;
import ma.emsi.graphhotel.dto.ChambreRequestDTO;
import ma.emsi.graphhotel.dto.ClientRequestDTO;
import ma.emsi.graphhotel.dto.ReservationRequestDTO;
import ma.emsi.graphhotel.services.ChambreService;
import ma.emsi.graphhotel.services.ClientService;
import ma.emsi.graphhotel.services.ReservationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class GraphqlHotelMainApp {
	private final ClientService clientService;
	private final ChambreService chambreService;
	private final ReservationService reservationService;

	public static void main(String[] args) {
		SpringApplication.run(GraphqlHotelMainApp.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			// Insérer des clients exemples
			clientService.save(new ClientRequestDTO("Alice Dupont", "alice.dupont@example.com", "0678123456"));
			clientService.save(new ClientRequestDTO("Bob Martin", "bob.martin@example.com", "0756123489"));
			clientService.save(new ClientRequestDTO("Claire Leroy", "claire.leroy@example.com", "0665123478"));
			clientService.save(new ClientRequestDTO("David Morel", "david.morel@example.com", "0768123490"));
			clientService.save(new ClientRequestDTO("Eva Bernard", "eva.bernard@example.com", "0654123450"));
			System.out.println("Clients exemples insérés avec succès.");

			// Insérer des chambres exemples
			chambreService.save(new ChambreRequestDTO(1L, "SIMPLE", 250.0, true));
			chambreService.save(new ChambreRequestDTO(2L, "DOUBLE", 150.0, false));
			chambreService.save(new ChambreRequestDTO(3L, "SIMPLE", 100.0, true));
			chambreService.save(new ChambreRequestDTO(4L, "SIMPLE", 300.0, true));
			chambreService.save(new ChambreRequestDTO(5L, "DOUBLE", 180.0, false));
			System.out.println("Chambres exemples insérées avec succès.");

			// Insérer des réservations exemples
			reservationService.save(new ReservationRequestDTO(1L, 3L, "2024-12-10", "2024-12-15", "Vacances d'hiver"));
			reservationService.save(new ReservationRequestDTO(2L, 1L, "2024-12-20", "2024-12-25", "Réunion d'affaires"));
			reservationService.save(new ReservationRequestDTO(3L, 4L, "2025-01-05", "2025-01-10", "Lune de miel"));
			System.out.println("Réservations exemples insérées avec succès.");
		};
	}
}
