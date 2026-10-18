package usc.etse.editoresymazmorras.dto;

import java.util.List;

// Payload de guardado del editor: reemplaza el mapa entero (bulk, más simple que celda a celda).
public record GuardarMapaDTO(List<CeldaDTO> celdas) {
}
