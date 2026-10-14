package usc.etse.editoresymazmorras.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usc.etse.editoresymazmorras.model.Mazmorra;

public interface MazmorraRepository extends JpaRepository<Mazmorra, Long> {
}
