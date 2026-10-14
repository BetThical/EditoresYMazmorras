package usc.etse.editoresymazmorras.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usc.etse.editoresymazmorras.model.Voto;
import usc.etse.editoresymazmorras.model.VotoId;

public interface VotoRepository extends JpaRepository<Voto, VotoId> {

    // Recuento de votos por signo: con esto se recalculan up/down de mazmorra.
    long countByIdMazmorraAndValor(Long mazmorra, short valor);
}
