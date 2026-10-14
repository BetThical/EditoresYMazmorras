package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

// Clave de un voto: una fila por (mazmorra, usuario) = un voto por usuario.
@Embeddable
public class VotoId implements Serializable {

    @Column(name = "mazmorra")
    private Long mazmorra;

    @Column(name = "usuario")
    private UUID usuario;

    public VotoId() {
    }

    public VotoId(Long mazmorra, UUID usuario) {
        this.mazmorra = mazmorra;
        this.usuario = usuario;
    }

    public Long getMazmorra() {
        return mazmorra;
    }

    public void setMazmorra(Long mazmorra) {
        this.mazmorra = mazmorra;
    }

    public UUID getUsuario() {
        return usuario;
    }

    public void setUsuario(UUID usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VotoId that)) {
            return false;
        }
        return Objects.equals(mazmorra, that.mazmorra) && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mazmorra, usuario);
    }
}
