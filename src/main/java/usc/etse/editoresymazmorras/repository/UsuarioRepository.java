package usc.etse.editoresymazmorras.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import usc.etse.editoresymazmorras.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}
