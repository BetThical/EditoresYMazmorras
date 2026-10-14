package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

// Una casilla del mapa (tabla elementomazmorra).
// Clave = mazmorra + coordenadas (x, y, planta); el botín va aparte en cofre.
@Entity
@Table(name = "elementomazmorra")
public class ElementoMazmorra {

    // Posición de la casilla dentro del mapa.
    @EmbeddedId
    private ElementoMazmorraId id;

    // Se persiste como varchar gracias a STRING (respeta el varchar(64) de la BD).
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 64)
    private TipoElemento tipo;

    public ElementoMazmorraId getId() {
        return id;
    }

    public void setId(ElementoMazmorraId id) {
        this.id = id;
    }

    public TipoElemento getTipo() {
        return tipo;
    }

    public void setTipo(TipoElemento tipo) {
        this.tipo = tipo;
    }
}
