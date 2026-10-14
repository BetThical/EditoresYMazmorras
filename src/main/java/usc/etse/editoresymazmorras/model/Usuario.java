package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {
    // Usuario mínimo: solo identidad para auth por X-Usuario-Id.

    @Id
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 32)
    private String nombre;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
