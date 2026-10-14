package usc.etse.editoresymazmorras.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import usc.etse.editoresymazmorras.model.ElementoMazmorra;
import usc.etse.editoresymazmorras.model.ElementoMazmorraId;

public interface ElementoMazmorraRepository extends JpaRepository<ElementoMazmorra, ElementoMazmorraId> {

    // Todo el mapa de una mazmorra (el editor lo pide de una vez).
    List<ElementoMazmorra> findByIdMazmorra(Long mazmorra);

    void deleteByIdMazmorra(Long mazmorra);
}
