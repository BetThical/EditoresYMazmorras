package usc.etse.editoresymazmorras.dto;

import java.util.List;
import java.util.UUID;

// Detalle completo: cabecera + mapa entero para pintar el editor.
public record MazmorraDetalleDTO(Long id, String nombre, long upvotes, long downvotes, UUID creador,
        List<CeldaDTO> celdas) {
}
