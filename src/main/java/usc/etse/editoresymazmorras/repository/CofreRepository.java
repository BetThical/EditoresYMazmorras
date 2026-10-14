package usc.etse.editoresymazmorras.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import usc.etse.editoresymazmorras.model.Cofre;
import usc.etse.editoresymazmorras.model.ElementoMazmorraId;

public interface CofreRepository extends JpaRepository<Cofre, ElementoMazmorraId> {

    List<Cofre> findByIdMazmorra(Long mazmorra);

    void deleteByIdMazmorra(Long mazmorra);
}
