package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Un voto (+1 / -1) de un usuario a una mazmorra (tabla voto).
// PK (mazmorra, usuario) = un voto por usuario; upsert para cambiarlo.
@Entity
@Table(name = "voto")
public class Voto {

    // Quién vota qué.
    @EmbeddedId
    private VotoId id;

    // +1 (up) o -1 (down). Los contadores de mazmorra se derivan de aquí.
    @Column(name = "valor", nullable = false)
    private short valor;

    public VotoId getId() {
        return id;
    }

    public void setId(VotoId id) {
        this.id = id;
    }

    public short getValor() {
        return valor;
    }

    public void setValor(short valor) {
        this.valor = valor;
    }
}
