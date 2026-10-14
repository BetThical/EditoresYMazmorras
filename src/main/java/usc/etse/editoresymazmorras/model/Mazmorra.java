package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "mazmorra")
public class Mazmorra {
    // Cabecera de la mazmorra: el mapa vive en elementomazmorra/cofre.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;

    // Contadores cacheados (la fuente de verdad es la tabla voto).
    @Column(name = "upvotes", nullable = false)
    private long upvotes = 0;

    @Column(name = "downvotes", nullable = false)
    private long downvotes = 0;

    // Dueño: UUID suelto (sin @ManyToOne) para no acoplar el agregado.
    @Column(name = "creador", nullable = false)
    private UUID creador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public long getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(long downvotes) {
        this.downvotes = downvotes;
    }

    public UUID getCreador() {
        return creador;
    }

    public void setCreador(UUID creador) {
        this.creador = creador;
    }
}
