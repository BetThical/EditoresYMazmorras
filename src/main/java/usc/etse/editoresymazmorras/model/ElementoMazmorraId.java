package usc.etse.editoresymazmorras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

// PK compuesta de casilla y cofre: (mazmorra, x, y, planta).
// @Embeddable = se incrusta como id en ambas entidades; equals/hashCode para identidad.
@Embeddable
public class ElementoMazmorraId implements Serializable {

    @Column(name = "mazmorra")
    private Long mazmorra;

    @Column(name = "posx")
    private Integer posx;

    @Column(name = "posy")
    private Integer posy;

    @Column(name = "planta")
    private Integer planta;

    public ElementoMazmorraId() {
    }

    public ElementoMazmorraId(Long mazmorra, Integer posx, Integer posy, Integer planta) {
        this.mazmorra = mazmorra;
        this.posx = posx;
        this.posy = posy;
        this.planta = planta;
    }

    public Long getMazmorra() {
        return mazmorra;
    }

    public void setMazmorra(Long mazmorra) {
        this.mazmorra = mazmorra;
    }

    public Integer getPosx() {
        return posx;
    }

    public void setPosx(Integer posx) {
        this.posx = posx;
    }

    public Integer getPosy() {
        return posy;
    }

    public void setPosy(Integer posy) {
        this.posy = posy;
    }

    public Integer getPlanta() {
        return planta;
    }

    public void setPlanta(Integer planta) {
        this.planta = planta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementoMazmorraId that)) {
            return false;
        }
        return Objects.equals(mazmorra, that.mazmorra) && Objects.equals(posx, that.posx)
                && Objects.equals(posy, that.posy) && Objects.equals(planta, that.planta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mazmorra, posx, posy, planta);
    }
}
