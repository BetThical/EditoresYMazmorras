package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

// Botín de una casilla COFRE (tabla cofre).
// Solo hay fila aquí si el elemento es de tipo COFRE; comparte su PK.
@Entity
@Table(name = "cofre")
public class Cofre {

    // Misma PK que su casilla (mazmorra, x, y, planta): FK implícita 1-a-1.
    @EmbeddedId
    private ElementoMazmorraId id;

    // Array de Postgres (varchar(64)[]): necesita el type ARRAY de Hibernate 6.
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "objetos", nullable = false, columnDefinition = "character varying(64)[]")
    private String[] objetos = new String[0];

    public ElementoMazmorraId getId() {
        return id;
    }

    public void setId(ElementoMazmorraId id) {
        this.id = id;
    }

    public String[] getObjetos() {
        return objetos;
    }

    public void setObjetos(String[] objetos) {
        this.objetos = objetos;
    }
}
