package usc.etse.editoresymazmorras.dto;

import java.util.UUID;
import usc.etse.editoresymazmorras.model.Mazmorra;

// Cabecera de mazmorra sin mapa: respuestas de crear y votar.
public record MazmorraDTO(Long id, String nombre, long upvotes, long downvotes, UUID creador) {

    public static MazmorraDTO de(Mazmorra m) {
        return new MazmorraDTO(m.getId(), m.getNombre(), m.getUpvotes(), m.getDownvotes(), m.getCreador());
    }
}
