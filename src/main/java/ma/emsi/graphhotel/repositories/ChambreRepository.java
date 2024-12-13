package ma.emsi.graphhotel.repositories;

import ma.emsi.graphhotel.entities.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByDisponible(boolean disponible);
}
